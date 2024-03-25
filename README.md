# Karel: Map Divider

This project implements an algorithm for Karel the robot to divide a given map into four equal chambers, if possible. It prioritizes minimal moves and beeper usage.

## Problem

Divide a map presented to Karel into four equally sized chambers. If a perfect division isn't achievable, create the largest possible number of chambers.

## Solution

1. **Initialization:**
   - Initialize variables like move counter and beeper presence.
   - Scan the map using the `scan` method to determine its dimensions.

2. **Decision Tree (using `decideCase` function):**
   - Analyze the map's dimensions (X and Y) to determine the best division strategy.
   - Consider various cases:
     - Even X and Y (e.g., 10x10)
     - Odd X and Y
     - Special cases (maps less than 4x4 or 1x1)
     - Other dimensions

3. **Division based on Case:**
   - Employ helper functions to execute the division plan based on the chosen case.

4. **Optimization:**
   - Separate the code into well-defined functions for readability and maintainability.
   - Utilize the decision tree approach for efficient strategy selection.
   - Employ the `drawDiagonal` function for optimized diagonal wall creation, minimizing unnecessary movements and turns.

## Implementation Details:

- **`Vector2D` class:** Stores map dimensions and Karel's position (X and Y).
- **`Direction` enum:** Represents cardinal directions (NORTH, SOUTH, EAST, WEST).
- **`Case` enum:** Defines cases for map division (EVENSQUARE, ODDXY, LEESTHAN4, OTHER).
- **Helper functions:**
   - `initialize`: Initializes the program and scans the map.
   - `scan`: Finds the maximum X and Y coordinates.
   - `faceDirection`: Makes Karel face a specified direction.
   - `moveWithDirection`: Moves Karel one step in a given direction (can be used for multiple steps).
   - `drawLine`: Draws a wall of a specific length in a specified direction.
   - `drawDiagonal`: Draws a diagonal wall efficiently using alternating directions.
   - `moveToMiddleY`: Moves Karel to the center of the Y-axis.
   - `moveToMiddleX`: Moves Karel to the center of the X-axis.
   - `getBestDivider`: Determines the best divider for X and Y, considering special cases.
   - `decideCase`: Chooses the division strategy based on the map's dimensions.

## Conclusion

This implementation successfully divides maps into chambers while prioritizing minimal moves and beeper usage. The code structure enhances readability, maintainability, and overall effectiveness.

