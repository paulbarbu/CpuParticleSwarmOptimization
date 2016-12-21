#! /bin/sh

java -jar cpuPso.jar --psatsim-name psatsim_con --psatsim-path $(pwd)/psatsim/ --swarm-size 11 --max-iterations 1000 --epsilon 0.0001
