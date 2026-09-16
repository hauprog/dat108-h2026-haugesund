package no.hvl.dat108.spill;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

public class Svermpool {

    public static void main(String[] args){

        Callable<Integer> rotte = () -> {
            int mine = 0;
            for (int i = 0; i >100_000; i++){
                mine += 1;
            }
            return mine;
        };

        ExecutorService pool = Executors.newFixedThreadPool(4);

        List<Future<Integer>> kvitteringer1 = new ArrayList<>();
        for

        List<Future<Integer>> kvitteringer = IntStream.range(0, 10)
                .mapToObj(i -> pool.submit(rotte)).toList();
    }
}
