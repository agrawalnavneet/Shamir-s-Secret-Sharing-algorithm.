# Shamir Secret Sharing Assignment

## Folder Structure

```
shamir-secret-sharing/
├── pom.xml                # Maven build file
├── README.md              # Project instructions
├── testcase1.json         # First test case (input)
├── testcase2.json         # Second test case (input)
└── src/
    └── ShamirSecret.java  # Main Java implementation (no comments)
```

## How to Build and Run

1. **Install dependencies**
   - Make sure you have Maven and Java (JDK 8+) installed.
   - The project uses the `org.json` library and `exp4j` (if you use expressions), both included in `pom.xml`.

2. **Compile and Run**
   - Open a terminal and navigate to the `shamir-secret-sharing` directory.
   - Run the following command:
     ```sh
     javac -cp "src:$(mvn dependency:build-classpath -Dmdep.outputFile=cp.txt >/dev/null && cat cp.txt)" src/ShamirSecret.java
     java -cp "src:$(cat cp.txt)" ShamirSecret
     ```
   - This will print the secret and wrong shares for both test cases.

## Output Format
- For each test case, the program prints:
  1. The secret (constant term of the polynomial)
  2. Either `No wrong shares` or `Wrong shares: x1, x2, ...` (the x values of wrong shares)

EX:  2
No wrong shares
79836264049850
Wrong shares: 8

## Notes
- Place your `testcase1.json` and `testcase2.json` files in the `shamir-secret-sharing` directory (same as `pom.xml`).
- The code is now free of comments for clean submission.
- If you want to use a package structure, move `ShamirSecret.java` to `src/main/java/your/package/` and update the class/package declaration accordingly. 
