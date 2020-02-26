# stressTest
Android app for stressing and benchmarking your processor

I was searching for a way to put load on the processor in my new phone. In F-Droid, I saw no available option, apart from one mining program that did not start for me. Thus stressTest was started. The name obfuscates that the purpose of the app is also to benchmark the processor, it might get changed later.

## How it works

stressTest opens a basic UI with one button. If it gets pressed, as many threads as there are processor cores in the phone start running a benchmark. Initially, that's a [fannkuch-redux implementation](http://benchmarksgame.wildervanck.eu/fannkuchredux-java-1.html) from *The Computer Language
Benchmarks Game*. 

After the benchmark is run a dialog box will report how long it took. 

In my tests, this scheme did work to show differences between phones and to put load on all processors. 

## Todo

Modern phones will be too fast for the current implementation. The benchmark could be run repeatedly on them, but that would make it rather annoyingto test it on older phones. It should be switched out with a different algorithm that achieves some progress that can be reported and gets run for a fixed amount of time, instead of measuring how long it took.
