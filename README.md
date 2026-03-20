# ☕ Vanguard Brew: Java Katas

**Master the Art of Modern Java, One Sip at a Time.**

Welcome to **Vanguard Brew**, a high-octane collection of coding katas designed to sharpen your skills with **Java 25**.
Whether you’re here to master the Stream API, explore new language features, or just practice Test-Driven Development (
TDD), we’ve roasted the perfect set of challenges for you.

---

## 🚀 Getting Started

### 1. Choose Your Blend (Clone the Repo)

Pick the method that fits your workflow:

* **Fork & Pull:** Best for tracking your progress and getting upstream updates.
    * [Learn how to fork a repo](https://docs.github.com/en/get-started/quickstart/fork-a-repo) (GitHub Official)
* **Clone:** Quickest way to get the code locally:
    ```bash
    git clone git@github.com:olasch/vanguard-brew.git
    ```
* **Download ZIP:** For a one-time "tasting" session without Git history.

### 2. Prepare the Station (Build)

This project is built with **Maven**.

> [!NOTE]
> You can use [Maven Wrapper](https://maven.apache.org/tools/wrapper/) for easier bootstrapping.

> [!IMPORTANT]
> Since we use a modular framework, you must install the core logic first:
> ```bash
> cd framework
> mvn install
> ```
> And the parents that are needed, from root:
> ```bash
> mvn -N install
> cd kata
> mvn -N install
> ```
> Or if you want to install everything, from root:
> ```bash
> mvn install -DskipTests
> ```

### 3. Select Your Roast (Pick a Category)

Each "Kata Category" is a self-contained module with a standard layout:

```text
.../[kata-category]
 ├── src/main  <-- Your implementation goes here ☕
 └── src/test  <-- The "Taste Test" (The requirements)
```

---

## 🛠 The Workflow: "Brew, Sip, Refine"

1. **Run the Tests:** Open your IDE and run the test class for the Kata you want to solve. You’ll see them fail—that’s
   just the "bitter" start!
2. **Implement Logic:** Navigate to the corresponding class in `src/main` and write your implementation.
3. **Achieve Clarity:** Run the tests again. Once they turn **green**, your code is perfectly brewed.

### Running Katas via Terminal

If you prefer the command line, you can run specific "Flavor Profiles" (tags):

> [!NOTE]
> You need to be in the correct subdir for this work properly, from the root dir it runs all projects, but filters out
> everything that does not match the tag.

```bash
cd kata/collections-kata
mvn test -Dgroups=List
```

Or run an individual test:
> [!NOTE]
> You need to be in the correct subdir for this to work.

```bash
cd kata/string-kata
mvn test -Dtest='VowelCounterTest#testNoVowels'
```

---

## 🌳 Git Strategy: Master the Flow

To keep your "Brew" fresh and your history clean, we follow a professional Git workflow. Whether you are practicing solo
or "brewing" with friends, these patterns will keep your repository organized.

### 🔄 Staying Up-to-Date

Since this is a living project, new Katas (new roasts!) are added frequently. To pull the latest challenges without
breaking your work:

```bash
git remote add upstream git@github.com:olasch/vanguard-brew.git
git fetch upstream
git merge upstream/main
```

### 🛣 Choosing Your Path

* **Trunk-Based Development:** Best for single Katas. Commit directly to `main` for quick, iterative learning.
* **Feature Branches:** Best for completing an entire **KataSuite** (e.g., `feature/map-katas`).
* *Pro Tip:* Use branches if you are coding with friends! Open a **Pull Request (PR)** to review each other's logic
  before merging.

### 📝 Commits & The Art of the `amend`

We value a clean history. Write descriptive, imperative commit messages (e.g., `feat: implement positive integer sort`).

If you made a typo or forgot a file in your last commit, don't create a new one! Use:

```bash
git add .
git commit --amend --no-edit
```

*This keeps your history from being cluttered with "oops" or "fix" commits.*

### 🔨 The Squash & Merge

Before merging a feature branch into `main`, **squash** your commits. This collapses a dozen "work-in-progress" commits
into one clean, functional milestone.

* If using GitHub, select **"Squash and merge"** on the PR.
* If using CLI: `git rebase -i main` and pick the `squash` option for your commits.

---

## 📋 The Menu: Kata Suites

In most modern IDEs (IntelliJ, VS Code), you can run these entire suites with a single click to validate an entire
topic.

| Suite                        | Focus Area                                                                            |
|:-----------------------------|:--------------------------------------------------------------------------------------|
| **`StringKatasSuite`**       | Text manipulation, RegEx, and modern String manipulation.                             |
| **`ListKatasSuite`**         | Filtering, sorting, and transforming ordered data.                                    |
| **`MapKatasSuite`**          | Key-value logic and complex data structures.                                          |
| **`SetKatasSuite`**          | Uniqueness, membership, and collection logic.                                         |
| **`DateKatasSuite`**         | Calendar-based logic involving `LocalDate`, `Period`, and chronological calculations. |
| **`TimeKatasSuite`**         | Clock-based logic involving `LocalTime`, and precise `Duration` measurements.         |
| **`ConstructingKatasSuite`** | How to instanciate objects, and validations before                                    |
| **`SealingKatasSuite`**      | Lock down domain models with sealed classes and exhaustive pattern matching           |
---

## 🛡 Why "Vanguard"?

This project isn't just about "basic" Java. It’s built for **Java 25**, meaning we encourage:

* **Functional Interfaces** (The `@FunctionalInterface` style).
* **Pattern Matching** for cleaner logic.
* **Concise Streams** for elegant data processing.

---

## 📚 Git Resources

* **[Pro Git Book](https://git-scm.com/book/en/v2):** The ultimate "Bible" of Git.
* **[GitHub Skills](https://skills.github.com/):** Interactive courses for branching and PRs.
* **[Conventional Commits](https://www.conventionalcommits.org/):** A standard for meaningful commit messages.
* **[Atlassian Git Tutorial](https://www.atlassian.com/git/tutorials):** Visual guides on Rebase vs. Merge.

---

*“Code is like coffee: it’s only good if it’s clean and gives you a boost.”*
