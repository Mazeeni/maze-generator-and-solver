import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.awt.image.BufferedImage;
import java.util.Stack;
import javax.imageio.ImageIO;

public class Maze {

  private final char BLOCK_CHARACTER = '█';
  private final int length;
  private final int height;
  private Block[][] grid;
  private final Random rand = new Random();

  public Maze(int length, int height, String algorithm) {
    this.length = length;
    this.height = height;
    grid = new Block[length][height];

    initialiseGrid();

    if (algorithm.equals("prim")) {
      generateWithPrim();
    } else if (algorithm.equals("backtracking")) {
      generateWithRecursiveBacktracking();
    } else {
      throw new UnsupportedOperationException("Invalid algorithm");
    }

    // completes the maze by adding exit point (i.e. near bottom right)
    unsetBlock(length - 1, height - 2);
    unsetBlock(length - 2, height - 2);
  }

  // Uses an adaptation of Prim's algorithm
  private void generateWithPrim() {
    List<Block> fringeBlocks = new ArrayList<>();
    fringeBlocks.add(new Block(1, 1));

    while (!fringeBlocks.isEmpty()) {
      Block randEnd = fringeBlocks.get(rand.nextInt(fringeBlocks.size()));
      if (!unsetRandomNeighbour(randEnd.getX(), randEnd.getY(), fringeBlocks)) {
        fringeBlocks.remove(randEnd);
      }
    }
    System.out.println("Maze generated using Prim's Algorithm!");
  }

  private void generateWithRecursiveBacktracking() {
    Stack<Block> blockStack = new Stack<>();
    // why didn't set work?
    List<Block> visited = new ArrayList<>();
    blockStack.push(new Block(1, 1));

    while (!blockStack.isEmpty()) {
      Block currBlock = blockStack.peek();
      if (!carveRandomPassage(currBlock.getX(), currBlock.getY(), blockStack, visited)) {
        visited.add(currBlock);
        blockStack.pop();
      }
    }
  }

  // look for more readable way to pick random neighbour
  // possible change of variable name neighbour for readability
  private boolean unsetRandomNeighbour(int x, int y, List<Block> fringeBlocks) {
    List<Block> neighbours = new ArrayList<>();
    neighbours.add(new Block(x, y - 1));
    neighbours.add(new Block(x, y + 1));
    neighbours.add(new Block(x - 1, y));
    neighbours.add(new Block(x + 1, y));

    while (!neighbours.isEmpty()) {
      Block neighbour = neighbours.get(rand.nextInt(neighbours.size()));
      if (fringeBlocks.contains(neighbour)) {
        neighbours.remove(neighbour);
        continue;
      } else if (checkBlock(neighbour.getX(), neighbour.getY())) {
        unsetBlock(neighbour.getX(), neighbour.getY());
        fringeBlocks.add(neighbour);
        return true;
      }
      neighbours.remove(neighbour);
    }
    return false;
  }

  private boolean carveRandomPassage(int x, int y, Stack<Block> blockStack, List<Block> visited) {
    List<Block> neighbours = new ArrayList<>();
    neighbours.add(new Block(x, y - 1));
    neighbours.add(new Block(x, y + 1));
    neighbours.add(new Block(x - 1, y));
    neighbours.add(new Block(x + 1, y));

    while (!neighbours.isEmpty()) {
      Block neighbour = neighbours.get(rand.nextInt(neighbours.size()));
      if (visited.contains(neighbour)) {
        neighbours.remove(neighbour);
        continue;
      } else if (checkBlock(neighbour.getX(), neighbour.getY())) {
        unsetBlock(neighbour.getX(), neighbour.getY());
        blockStack.push(neighbour);
        return true;
      }
      neighbours.remove(neighbour);
    }
    System.out.println("got here");
    return false;
  }

  private boolean isPerimeter(int x, int y) {
    return x == 0 || y == 0 || x == length - 1 || y == height - 1;
  }

  private boolean checkBlock(int x, int y) {
    if (isPerimeter(x, y)) {
      return false;
    }
    if (!blockIsSet(x, y - 1) && !blockIsSet(x - 1, y)) {
      return false;
    } else if (!blockIsSet(x, y - 1) && !blockIsSet(x + 1, y)) {
      return false;
    } else if (!blockIsSet(x + 1, y) && !blockIsSet(x, y + 1)) {
      return false;
    } else if (!blockIsSet(x, y + 1) && !blockIsSet(x - 1, y)) {
      return false;
    } else if (!blockIsSet(x, y - 1) && !blockIsSet(x, y + 1)) {
      return false;
    } else if (!blockIsSet(x - 1, y) && !blockIsSet(x + 1, y)) {
      return false;
    }
    return true;
  }

  private void setBlock(int x, int y) {
    grid[x][y].set();
  }

  private void unsetBlock(int x, int y) {
    grid[x][y].unSet();
  }

  private boolean blockIsSet(int x, int y) {
    return grid[x][y].isSet();
  }

  // initialises grid with all elements in a 'set' state
  private void initialiseGrid() {
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < length; j++) {
        grid[j][i] = new Block(j, i);
      }
    }
    // sets entry point for maze
    unsetBlock(0, 1);
    unsetBlock(1, 1);
  }

  // output maze to command line
  public String printMaze() {
    StringBuilder b = new StringBuilder();
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < length; j++) {
        if (grid[j][i].isSet()) {
          System.out.print(BLOCK_CHARACTER);
          b.append(BLOCK_CHARACTER);
        } else {
          System.out.print(' ');
          b.append(' ');
        }
      }
      System.out.print("\n");
      b.append("\n");
    }

    return b.toString();
  }



  public void toImage(String name) {
    BufferedImage img = new BufferedImage(length, height, BufferedImage.TYPE_INT_ARGB);
    int a = 255;
    int r = 255;
    int g = 255;
    int b = 255;
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < length; j++) {
        if (!blockIsSet(j, i)) {
          int p = (a<<24) | (r<<16) | (g<<8) | b;
          img.setRGB(j, i, p);
        }
      }
    }
    int p = (a<<24) | (g<<8);
    img.setRGB(0, 1, p);
    img.setRGB(length - 1, height - 2, p);
    String pathname = "generated-mazes/";
    try {
      File f = new File(pathname + name);
      ImageIO.write(img, "png", f);
      System.out.println("Image conversion successful, output in: \"generated-mazes/" + name + "\"");
    } catch(IOException e){
      System.out.println(e);
    }

  }

}
