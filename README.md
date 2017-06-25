# Soviet lift simulator

## Info

This is simple Soviet Lift simulator

### Rules

1. We don't ever know, if there is somebody in elevator, so
   1. We always handle presses from inside the elevator
   2. If somebody presses floor number from inside the elevator doors are closed immidiately
2. If the elevator is moving — we don't accept any commands
3. If the doors are open
   1. If it's called from same floor from outside then it's not reacting
   2. If it's called to same floor from inside then it's not reacting
   3. If it's call from inside to another floor then 1.2
   4. If it's called from outside to another floor - then it's busy and is not reacting
4. Starting state:
   1. The elevator is on 1st floor
   2. Doors are closed
   3. As usual, we don't know if somebody inside

### Technical info

Elevator is accepting 3 types of commands:

1. `exit` : well, it's exit, you know…
2. `i <floor number>`: the elevator is called from inside to the floor `<floor number>`
3. `o <floor number>`: the elevator is called from outside to the floor `<floor number>`

Other commands are invalid

### Usage

```
Usage: lift-test [options]
  Options:
  * -h, --floor-height
      Floor height, m, should be >0
  * -f, --floors-num
      Number of floors in building, where 5<=f<=20
  * -t, --open-close-time
      Time, elevator needs to close the doors (s), should be >0
  * -s, --speed
      Speed, m/s, should be >0
```

### Build

```
./gradlew build
```

### Launch

```
java -jar build/libs/lift-test-liftsim-all.jar
```