CpuParticleSwarmOptimization
============================
Generate a set of "best" CPU configuration using Particle Swarm Optimization 

The definition of "best" is that the CPU should consume the least energy and process a high number of instructions-per-cycle (IPC).

This project uses the [psatsim](http://homepages.udayton.edu/~ttaha1/psatsim/) simulator along with 
the traces that come with it to simulate the different CPU configurations generated by the PSO algorithm.
Hence you'll need to download the simulator in order to make use this project.

Libraries used:
scala-xml used to read/write the simulator's XML files
jMetal used for its implementation of PSO (namely SMPSO)
JFreeChart used to plot the dominant CPU configurations

How to build & run
==========
You can build & run the project using ScalaIDE (based on Eclipse).
Or by following the instructions written in `src/Main.java`

LICENSE
=======
Copyright (c) 2016, Barbu Paul - Gheorghe
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
