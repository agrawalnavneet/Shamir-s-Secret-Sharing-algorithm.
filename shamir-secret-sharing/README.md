# Shamir Secret Sharing Assignment

## Folder Structure

```
shamir-secret-sharing/
├── pom.xml                # Maven build file
├── README.md              # Project instructions
├── testcase1.json         # First test case (input)
├── testcase2.json         # Second test case (input)
└── src/
    └── ShamirSecret.java  # Main Java implementation
```

## How to Build and Run

1. **Install dependencies**
   - Make sure you have Maven and Java (JDK 8+) installed.
   - The project uses the `org.json` library, which is included in `pom.xml`.

2. **Compile the project**
   ```sh
   cd shamir-secret-sharing
   mvn compile
   ```

3. **Run the program**
   ```sh
   mvn exec:java -Dexec.mainClass=ShamirSecret
   ```
   Or, if you want to run manually:
   ```sh
   javac -cp "path/to/json.jar" src/ShamirSecret.java
   java -cp "src:path/to/json.jar" ShamirSecret
   ```

4. **Expected Output**
   The program will print the secret (constant term) for both test cases, one per line.

## Notes
- Place your `testcase1.json` and `testcase2.json` files in the `shamir-secret-sharing` directory (same as `pom.xml`).
- If you want to use a package structure, move `ShamirSecret.java` to `src/main/java/your/package/` and update the class/package declaration accordingly.
