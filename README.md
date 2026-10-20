# MUSIKALIST: Songlist Management System for Musikalista

**Data Structures and Algorithms Final Project (Group 5)**
A console-based song request system for **Musikalista**, the music organization of De La Salle Lipa.

```
    __  __ _   _ ___ ___ _  __   _   _    ___ ___ _____
   |  \/  | | | / __|_ _| |/ /  /_\ | |  |_ _/ __|_   _|
   | |\/| | |_| \__ \| || ' <  / _ \| |__ | |\__ \ | |
   |_|  |_|\___/|___/___|_|\_\/_/ \_\____|___|___/ |_|
```

---

## Concepts of Data Structures and Algorithms

| Concept | Where it's used | Complexity |
|---|---|---|
| **Queue (FIFO)** | Manages song requests. *Enqueue* adds a request, *Peek* views the song playing, and *Dequeue* removes and plays the next request. | Enqueue / Dequeue / Peek: O(1) |
| **Binary Search** | Finds songs by title, artist, or song number in a list already sorted by that attribute. | O(log n) |
| **Merge Sort** | Arranges songs by title, artist, or genre, in ascending or descending order. | O(n log n) |

- **Queue:** `RequestQueue` stores `SongRequest` objects in a `Queue` (backed by a `LinkedList`), so requests are always played in the order they were made.
- **Binary Search:** `SongSearcher` sorts the list by the chosen attribute first, then searches it. For titles and artists, it also collects all neighboring matches so duplicates are not missed.
- **Merge Sort:** `SongSorter` is a custom recursive merge sort that accepts any `Comparator<Song>`, so one algorithm handles every sorting option.

---

## Features

### Main Menu

| # | Option                        | Description | Concept |
|---|-------------------------------|---|---|
| 1 | **Request Song**              | Records a student's song request and adds it to the end of the queue | Enqueue |
| 2 | **View Current Song Playing** | Shows the song that is currently playing | Peek |
| 3 | **Play Next Requested Song**  | Removes the oldest request from the queue and plays it | Dequeue (FIFO) |
| 4 | **View Requests**             | Displays all pending requests in order | Queue traversal |
| 5 | **View Song List**            | Displays every song in the song book | SongList |
| 6 | **Search Song List**          | Search by **Title**, **Artist**, or **Song Number** | Binary Search |
| 7 | **Sort Song List**            | Sort by **Title**, **Artist**, or **Genre** (ascending or descending) | Merge Sort |
| 8 | **Exit**                      | Closes the program | n/a |

### Song Information

| Field | Details |
|---|---|
| Song Number | Auto-generated, unique |
| Title | Required, up to 50 characters |
| Artist | Required, up to 50 characters |
| Genre | `ROMANCE`, `POP`, `ROCK`, `HIPHOP`, `INDIE` |

### Request Rules

- Each request records the **student name** and the **song** requested.
- Student names may contain only letters, spaces, periods, apostrophes, and hyphens.
- A song that is already in the queue cannot be requested again.
- A student can have at most **3** pending requests.
- The queue holds at most **100** requests.

---

## Project Structure

```
musikalista-songlist-management-system/
├── Main.java                  # Main menu and program flow
├── models/
│   ├── Genre.java             # Genre enum
│   ├── Song.java              # Song model
│   └── SongRequest.java       # Request model (student + song)
├── managers/
│   ├── SongBook.java          # Song list management (view, search, sort)
│   ├── RequestQueue.java      # Queue of song requests (FIFO)
│   └── FileHandler.java       # Reads and writes the data files
├── algorithms/
│   ├── SongSearcher.java      # Binary search
│   └── SongSorter.java        # Merge sort
├── comparators/
│   ├── TitleComparator.java
│   ├── ArtistComparator.java
│   └── GenreComparator.java
└── utils/
    ├── DataValidation.java    # Input validation
    └── DisplayUtils.java      # Menus, tables, and formatting
```

---

## Data Storage

The program saves its data in plain text files, created automatically in the folder where the program is run:

| File | Format | Example |
|---|---|---|
| `SONG_LIST.txt` | `songNumber\|title\|artist\|GENRE` | `1\|Perfect\|Ed Sheeran\|ROMANCE` |
| `REQUEST_LIST.txt` | `requestNumber\|studentName\|songNumber` | `1\|Maria Santos\|1` |

Songs and pending requests are loaded when the program starts, so the queue survives restarts.

---

## How to Run

**Requirements:** Java 16 or newer (JDK).

From the project's root folder:

```bash
# Compile
javac -d out $(find . -name "*.java")

# Run
java -cp out Main
```

On Windows (Command Prompt):

```bat
dir /s /b *.java > sources.txt
javac -d out @sources.txt
java -cp out Main
```

Or open the folder in **VS Code** / **IntelliJ IDEA** and run `Main.java`.

> **Tip:** Run the program from the project root so `SONG_LIST.txt` and `REQUEST_LIST.txt` are created and read from the same place every time.

---

## Sample Session

```
============================================================
                       WELCOME, RAKS!
============================================================

------------------------- Requests -------------------------
 [1] Add New Song
 [2] View Current Song Playing
 [3] Play Next Requested Song
 [4] View Requests

------------------------ Song List -------------------------
 [5] View Song List
 [6] Search Song List
 [7] Sort Song List

---------------------- Close Program -----------------------
 [8] Exit
============================================================
Select Option:
```

---

## Group 5 Members and Roles

| Member | Responsibilities |
|---|---|
| **Dapito** | `Main` (main menu and program flow) |
| **Alcala** | `utils`: `DataValidation`, `DisplayUtils`<br>`managers`: `RequestQueue`, `SongBook`<br>`algorithms`: `SongSearcher`, `SongSorter` |
| **Maputi** | `models`: `Genre`, `Song`, `SongRequest` |
| **Pateña** | `managers`: `FileHandler` |
| **De Luna** | `comparators`: `ArtistComparator`, `GenreComparator`, `TitleComparator` |

---

## Course Information

- **Course:** Data Structures and Algorithms
- **School:** De La Salle Lipa
- **Project:** Final Project
