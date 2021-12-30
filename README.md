# objective-secured

Controlling marker lights for 40k battlefield objectives

## Overview

The Warhammer 40k ninth edition rulebook offers several different
missions which can be played on a 44"x30" battlefield.

These missions use objectives on the battlefield in fixed locations
which can be captured and/or held by either players army (see poorly
cropped [mission images](os-app/assets/deployment-maps)).

I built a battlefield using mdf, extruded aluminium struts and a while
lot of epoxy glue. I drilled holes in all the objective locations and
then wired up RGB LEDs to shine through them from underneath.

I made objective markers by taking selenite crystal towers (they're
not magic, but they do act like fibre-optics) and sticking them onto
40mm bases which I cut holes in so the light could shine through.

This repo contains the code for controlling the lights on the
battlefield.

The whole project is very DIY, good luck, ymmv.

## Components

There are three parts to this project: the Controller, the Server and
the App.

The Controller runs on an Arduino which is connected by USB to a
Raspberry Pi which is running the Server. The App runs on an Android
phone and communicates with the Server over HTTP (on your local
network).

You could use the Controller on its own if you want to handle sending
it commands yourself, or you could use the Controller and the Server
if you want an HTTP API. Using all three components lets you send
requests to the Server using a basic but functional Android app.

### The Controller

In `arduino-controller/` there is a single Arduino sketch
(`arduino-controller.ino`) which is responsible for turning on and off
the individual LEDs on the board using the Adafruit NeoPixel library.

It's not great but it works™.

The Arduino will listen on the serial port and expects a command
string followed by a newline character. The command should be a
pipe-separated string consisting of an `action`, a `mission`, an
`objective` and a `colour` if required.

| section | allowed values |
| --- | ----------- |
| `action` | either `ON` or `OFF` |
| `mission` | one of `incisive-attack`, `outriders`, `encircle`, `divide-and-conquer`, `crossfire`, `centre-ground`, `forward-push`, `ransack`, `shifting-front` |
| `objective` | zero-indexed objective number, `0-3` for all missions except ransack which has 6 objectives, so `0-5` |
| `colour` | any hex colour (though dark ones wont work very well), only used when `action` is `ON` |

Examples:

`"ON|incisive-attack|3|#00FFFF\n"`

This command turns on the fourth objective for the Incisive Attack
mission with a nice bright turquoise.

`"OFF|ransack|4\n"`

This command turns off the fifth objective for the Ransack mission.

### The Server

The Server is a Clojure application that exposes an HTTP API on port
`3000`. It handles requests from the App and sends commands to the
Controller over USB using a [hacky python script](buffer.py).

The requests should be POSTs containing a JSON body with `mission-id`
and `objectives` fields.

| field | allowed values |
| --- | ----------- |
| `mission-id` | one of `incisive-attack`, `outriders`, `encircle`, `divide-and-conquer`, `crossfire`, `centre-ground`, `forward-push`, `ransack`, `shifting-front` |
| `objectives` | array of zero-indexed objective numbers, `0-3` for all missions except ransack which has 6 objectives, so `0-5` |

*NOTE*: we are specifying a _list_ of objectives here instead of just
a single one (like the Controller does). This makes it possible to
turn on/off all lights for a mission with a single request.

You can also manually send requests using the swagger UI found at
`localhost:3000/index.html`. This is especially useful while testing.

The `build.sh` script will compile the application in a jar, the
`start.sh` and `stop.sh` scripts will cleanly turn it off and on.

### The App

The App is a Clojurescript re-frame application using React Native and
Expo to build an Android APK.

@TODO: bit more docs here would be helpful

To build: `expo build:android`
