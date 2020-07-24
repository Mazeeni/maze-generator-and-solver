public class maze {

  private final int length;
  private final int height;

  public maze(int length, int height, String algorithm) {
    this.length = length;
    this.height = height;
    if (algorithm.equals("prim")) {
      generateWithPrim();
    } else if (algorithm.equals("recursive-backtracking")) {
      generateWithRecursiveBacktracking();
    } else {
      throw new UnsupportedOperationException("Invalid algorithm: choose from \n1: \"prim\"");
    }
  }

  private void generateWithPrim() {

  }

  private void generateWithRecursiveBacktracking() {

  }
}
