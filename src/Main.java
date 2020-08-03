public class Main {

  public static void main(String[] args) {
    Maze primsTest = new Maze(40, 15, "prim");
    primsTest.toImage("prim.jpg");

    Maze recursiveTest = new Maze(40, 15, "backtracking");
    recursiveTest.toImage("recursive.jpg");
  }
}
