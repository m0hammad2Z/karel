import stanford.karel.SuperKarel;
import javafx.util.Pair;

public class Homework extends SuperKarel {

    // To determine the direction
    enum Direction {
        NORTH, SOUTH, EAST, WEST,
    }

    // To determine the case
    enum Case{
        EVENSQUARE, ODDXY, LEESTHAN8, OTHER
    }


    Vector2D currentPos = new Vector2D(1, 1);
    Vector2D maxPos = new Vector2D(1, 1);

    int moveCounter = 0;

    public void run() {

        // Initialize the program -> find the max x and y
        initialize();

        // If the world is 1x1 or 1x2 or 2x1, end the program
        if (maxPos.x + maxPos.y < 4) return;

        // Decide which case is the world
        Case worldCase = decideCase();

        // Draw the world
        switch (worldCase) {
            case LEESTHAN8:
                drawLessThan8();
                break;

            case ODDXY:
                drawOddXY();
                break;

            case EVENSQUARE:
                drawEvenXYSquare();
                break;


            case OTHER:
                drawOther();
                break;

        }

        System.out.println("Move Counter: " + moveCounter);
    }


    // ----- Divide the world --------- //


    // If the world is 2x2 or 2x3 or 3x2
    void drawLessThan8() {
        if (maxPos.x > maxPos.y) {
            moveToMiddleX();
            drawLine(Direction.SOUTH, maxPos.y - 1);
        } else {
            moveToMiddleY();
            drawLine(Direction.WEST, maxPos.x - 1);
        }
    }

    // In case it's even square -> draw diagonal
    void drawEvenXYSquare() {
        drawDiagonal(Direction.WEST, Direction.SOUTH);
        if (maxPos.x + maxPos.y == 4) return;
        moveWithDirection(Direction.NORTH, maxPos.y - 1);
        drawDiagonal(Direction.EAST, Direction.SOUTH);
    }

    // If the two numbers are odd
    void drawOddXY() {
        if (maxPos.x == 1 || maxPos.y == 1) {
            Pair<String, Integer> pair = getBestDivider(maxPos.x, maxPos.y);

            Direction dir = maxPos.x == 1 ? Direction.SOUTH : Direction.WEST;
            int val = maxPos.x == 1 ? maxPos.y : maxPos.x;

            int counter = (int) pair.getValue();

            for (int i = 0; i < counter; i++) {
                drawLine(dir, 0);
                moveWithDirection(dir, val / counter);
            }
            drawLine(dir, 0);
        } else {
            moveToMiddleX();
            drawLine(Direction.SOUTH, maxPos.y - 1);
            moveToMiddleY();
            drawLine(Direction.WEST, maxPos.x / 2);
            drawLine(Direction.EAST, maxPos.x - 1);
        }
    }

    // The all other cases
    void drawOther() {
        Pair<String, Integer> pair = getBestDivider(maxPos.x, maxPos.y);

        int val = pair.getKey().equals("x") ? maxPos.x : maxPos.y;
        int val2 = pair.getKey().equals("x") ? maxPos.y : maxPos.x;

        int counter = (int) pair.getValue();

        boolean flip = false;
        Direction drawDir;
        Direction moveDir;

        // if the world is 2 x prime number
        if ((maxPos.x == 2 || maxPos.y == 2) && counter == 1) {
            if (currentPos.x == 2) {
                moveToMiddleY();
                drawLine(Direction.WEST, maxPos.x - 1);
            } else {
                moveToMiddleX();
                drawLine(Direction.SOUTH, maxPos.y - 1);
            }
            return;
        }

        for (int i = 0; i < counter; i++) {
            if (pair.getKey().equals("x")) {
                drawDir = flip ? Direction.NORTH : Direction.SOUTH;
                moveDir = Direction.WEST;
            } else {
                drawDir = flip ? Direction.EAST : Direction.WEST;
                moveDir = Direction.SOUTH;
            }
            flip = !flip;

            drawLine(drawDir, val2 - 1);
            double v = Math.ceil((double) val / counter);
            if (i == counter - 1) v -= 1;
            moveWithDirection(moveDir, (int) v);
        }
    }




// ----- Helper functions ---------

    // Initialize the program -> find the max x and y
    void initialize() {
        currentPos.x = 1;
        currentPos.y = 1;

        moveCounter = 0;

        if (!beepersInBag())
            setBeepersInBag(1000);

        scan();
    }


    // Scans the whole world and finds the max x and y
    void scan() {
        while (frontIsClear()) {
            moveWithDirection(Direction.EAST);
        }

        faceDirection(Direction.NORTH);
        while (frontIsClear()) {
            moveWithDirection(Direction.NORTH);
        }

        maxPos.x = currentPos.x;
        maxPos.y = currentPos.y;
        System.out.println("xMax: " + maxPos.x + " yMax: " + maxPos.y);
    }

    // Make karel face a specific direction.
    void faceDirection(Direction direction) {
        switch (direction) {
            case NORTH:
                while (!facingNorth()) turnLeft();
                break;
            case SOUTH:
                while (!facingSouth()) turnLeft();
                break;
            case EAST:
                while (!facingEast()) turnRight();
                break;
            case WEST:
                while (!facingWest()) turnLeft();
                break;
        }
    }

    // Move karel one step
    void moveWithDirection(Direction dir) {
        switch (dir) {
            case NORTH:
                faceDirection(Direction.NORTH);
                move();
                currentPos.y += 1;
                break;
            case SOUTH:
                faceDirection(Direction.SOUTH);
                move();
                currentPos.y -= 1;
                break;
            case EAST:
                faceDirection(Direction.EAST);
                move();
                currentPos.x += 1;
                break;
            case WEST:
                faceDirection(Direction.WEST);
                move();
                currentPos.x -= 1;
                break;
        }
        moveCounter++;
    }

    // Move karel multiple steps
    void moveWithDirection(Direction dir, int steps) {
        while (steps > 0) {
            moveWithDirection(dir);
            steps--;
        }
    }

    // Draw a line wall
    void drawLine(Direction dir, int length) {
        for (int i = 0; i < length; i++) {
            if (noBeepersPresent())
                putBeeper();
            moveWithDirection(dir);
        }
        if (noBeepersPresent())
            putBeeper();
    }

    // Draw a diagonal wall
    void drawDiagonal(Direction dir1, Direction dir2) {
        while (true) {
            if (noBeepersPresent())
                putBeeper();
            moveWithDirection(dir1);
            moveWithDirection(dir2);
            if ((currentPos.x == 1 || currentPos.y == 1) || (currentPos.x == maxPos.x && currentPos.y == maxPos.y)) break;
        }
        if (noBeepersPresent())
            putBeeper();
    }

    // Move Karel to the center of Y
    void moveToMiddleY() {
        if (currentPos.y < maxPos.y / 2) {
            moveWithDirection(Direction.NORTH, maxPos.y / 2 - currentPos.y + 1);
        } else if (currentPos.y > maxPos.y / 2) {
            moveWithDirection(Direction.SOUTH, currentPos.y - maxPos.y / 2 - 1);
        }
    }

    // Move Karel to the center of X
    void moveToMiddleX() {
        if (currentPos.x < maxPos.x / 2) {
            moveWithDirection(Direction.EAST, maxPos.x / 2 - currentPos.x + 1);
        } else if (currentPos.x > maxPos.x / 2) {
            moveWithDirection(Direction.WEST, currentPos.x - maxPos.x / 2 - 1);
        }
    }

    // Get the best divider
    Pair<String, Integer> getBestDivider(int x, int y) {
        // Here we get the biggest divider of x and y
        int counterX = x % 4 == 0 ? 4 : x % 3 == 0 ? 3 : x % 2 == 0 ? 2 : 1;
        int counterY = y % 4 == 0 ? 4 : y % 3 == 0 ? 3 : y % 2 == 0 ? 2 : 1;

        // To handle some of the special cases
        if (x <= 4 || y <= 4) {
            counterX = x <= 4 ? 2 : counterX;
            counterY = y <= 4 ? 2 : counterY;

            // If the world is 2 x number do not divide it by 4 or 3
            if (x == 2) {
                counterX = 0;
            }
            if (y == 2) {
                counterY = 0;
            }
        }

        // Return the biggest value if both are equal
        if (x != 1 && y != 1)
            if (counterX == counterY) {
                return x > y ? new Pair<>("x", counterX) : new Pair<>("y", counterY);
            }

        return new Pair<>(counterX > counterY ? "x" : "y", Math.max(counterX, counterY));
    }

    Case decideCase() {
        if (maxPos.x * maxPos.y < 8 && maxPos.x * maxPos.y > 4) return Case.LEESTHAN8; // 2x2, 2x3, 3x2
        if (maxPos.x % 2 != 0 && maxPos.y % 2 != 0) return Case.ODDXY;  // 3x5, 5x3
        if (maxPos.x == maxPos.y) return Case.EVENSQUARE; // 4x4, 6x6
        return Case.OTHER; // 4x5, 5x4, 6x7, 7x6
    }



}




