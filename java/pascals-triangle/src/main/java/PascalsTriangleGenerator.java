import java.util.ArrayList;
import java.util.List;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PascalsTriangleGenerator {

    IntFunction<int[][]> strategy;

    public PascalsTriangleGenerator() {
        this.strategy = StrategyE.VIA_RECURSION;
    }

    public PascalsTriangleGenerator(IntFunction<int[][]> strategy) {
        this.strategy = strategy;
    }

    public int[][] generateTriangle(int r) {
        return strategy.apply(r);
    }

    enum StrategyE implements IntFunction<int[][]> {
        VIA_RECURSION {
            @Override
            public int[][] apply(int r) {
                if (r <= 0)
                    return new int[][]{};

                List<List<Integer>> loop = loop(r, 0, new ArrayList<List<Integer>>());

                int[][] pascalsRows = loop.stream().map(pascalRow -> pascalRow.stream().mapToInt(i -> i).toArray()).collect(Collectors.toList()).toArray(new int[][]{});

                return pascalsRows;
            }

            private List<Integer> nextRow(List<Integer> currentRow) {
                int first = currentRow.get(0);
                int last = currentRow.get(currentRow.size() - 1);

                List<Integer> nextRow = new ArrayList<>();
                nextRow.add(first);
                for (int i = 0; i < currentRow.size() - 1; i++) {
                    nextRow.add(Integer.sum(currentRow.get(i), currentRow.get(i + 1)));
                }
                nextRow.add(last);
                return nextRow;
            }

            /*
             * Stream.concat() is my way to add to merge two lists together since List's API doesn't have add() and return the List.
             */
            private List<List<Integer>> loop(int r, int c, List<List<Integer>> pascalsRows) {
                if (c == r)
                    return pascalsRows;
                if (c == 0)
                    return loop(r, c + 1, Stream.concat(pascalsRows.stream(), Stream.of(List.of(1))).collect(Collectors.toList()));
                else if (c == 1)
                    return loop(r, c + 1, Stream.concat(pascalsRows.stream(), Stream.of(List.of(1, 1))).collect(Collectors.toList()));
                else
                    return loop(r, c + 1, Stream.concat(pascalsRows.stream(), Stream.of(nextRow(pascalsRows.get(pascalsRows.size() - 1)))).collect(Collectors.toList()));
            }
        },
        SIMPLE {
            @Override
            public int[][] apply(int r) {
                int[][] pascalsRows = new int[r][];

                for (int i = 0; i < r; i++) {
                    pascalsRows[i] = new int[i + 1];
                    pascalsRows[i][0] = 1;
                    pascalsRows[i][i] = 1;

                    for (int j = 1; j < i; j++) {
                        pascalsRows[i][j] = pascalsRows[i - 1][j] + pascalsRows[i - 1][j - 1];
                    }
                }
                return pascalsRows;
            }
        }
    }
}