package no.hvl.dat108.spill;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.IntStream;

public class Svermpool {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        Callable<Integer> rotte = () -> {
            int mine = 0;
            for (int i = 0; i < 100_000; i++) {   // < , ikke >
                mine += 1;
            }
            return mine;
        };

        ExecutorService pool = Executors.newFixedThreadPool(4);

        List<Future<Integer>> kvitteringer = IntStream.range(0, 10)
                .mapToObj(i -> pool.submit(rotte))
                .toList();

        // Her er imperativ programmering kanskje å foretrekke for lesbarhetens skyld.
        List<Future<Integer>> kvitteringer2 = new ArrayList<>();
        for (int i = 0; i < 10; i++) kvitteringer2.add(pool.submit(rotte));

        // fredag: samle inn, planlagt feil, AtomicInteger, shutdown
        // Samle inn: get venter hvis svaret ikke er klart
        int total = 0;
        for (Future<Integer> f : kvitteringer)
            total += f.get();

        pool.shutdown();
        System.out.println("total: " + total + " (forventet 1_000_000.");
    }
}
