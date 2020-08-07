# stressTest
Android app for stressing and benchmarking your processor

I was searching for a way to put load on the processor in my new phone. In F-Droid, I saw no available option, apart from one mining program that did not start for me. Thus stressTest was started. The name obfuscates that the purpose of the app is also to benchmark the processor, it might get changed later.

## How it works

stressTest opens a basic UI with two buttons. If the first gets pressed, as many threads as there are processor cores in the phone start running a benchmark. Initially, that's a [fannkuch-redux implementation](https://benchmarksgame-team.pages.debian.net/benchmarksgame/program/fannkuchredux-java-1.html) from *The Computer Language
Benchmarks Game*. 

After the benchmark is run a dialog box will report how long it took.  In my tests, this scheme did work to show differences between phones and to put load on all processors. 

The second button will start the stresstest mode. The same algorithm will run, repeatedly, until the stop button gets pressed.

## Todo

It would be nice if during the benchmark the UI would show processor clock, load and the processor temperature. This seems to not be easy to achieve in Android though.
