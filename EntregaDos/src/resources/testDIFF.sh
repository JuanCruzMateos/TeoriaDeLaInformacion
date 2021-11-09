#! /usr/bin/bash

for tipo in Huffman Shannon RLC; do
    for arch in Argentina Hawai; do
        if diff "${arch}.txt" "recovery${tipo}${arch}.txt" > /dev/null; then
            echo " + Passed recovery${tipo}${arch}.txt";
        else
            echo " - Failed recovery${tipo}${arch}.txt";
        fi
    done
    if diff "imagen.raw" "recovery${tipo}imagen.txt" > /dev/null; then
       echo " + Passed recovery${tipo}imagen.txt";
    else
       echo " - Failed recovery${tipo}imagen.txt";
    fi

#    if diff "test.txt" "recovery${tipo}test.txt" > /dev/null; then
#       echo " + Passed recovery${tipo}test.txt";
#    else
#       echo " - Failed recovery${tipo}test.txt";
#    fi
done
