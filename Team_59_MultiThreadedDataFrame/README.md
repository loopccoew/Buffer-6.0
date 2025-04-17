Team Name: QuadCode

Team No. : 59

Team Members :
1. Siddhi Chaudhari (SY ENTC)
2. Arpita Barjibhe (SY COMP)
3. Devashree Ugane (SY ENTC)
4. Rucha Diwan (SY ENTC)

Problem Statement: Traditional Java DataFrames process data sequentially, causing inefficiencies with large datasets. Our project develops a custom data structure—a Multi-Threaded DataFrame— that uses parallel computing to accelerate sorting, filtering, and aggregation, improving scalability and performance.

Description: 
1. Parallel Sorting — Fast column-based sorting using multithreading.
2. Parallel Filtering — High-performance filtering with lambda-based conditions.
3. Parallel GroupBy & Aggregation — Supports sum, avg, min, and max over groups.
4. CSV Load & Export — Reads and writes CSV files with automatic null handling.
5. Benchmarking — Tracks performance of each major operation (in ms).

This custom DataFrame structure improves scalability and speed, making it ideal for lightweight data analysis in Java environments.

Data Structures Used:

1. ArrayList – For storing column names and column-wise data (fast indexed access).
2. LinkedHashMap – For maintaining insertion order in columns and benchmarks.
3. HashMap – For internal row representation and group-by aggregations.
4. List<Row> – For row-level operations like filtering and sorting.
5. Map<String, String> – Represents individual rows for easy access by column name.

Core Logic and Implementation:
1. Parallel Sorting : Data is divided into smaller chunks and each chunk is sorted in a separate thread. The results are merged after all threads complete.
2. Parallel Filtering : Rows are split across threads and filtered using a custom condition. The filtered data is collected and returned as a new DataFrame.
3. GroupBy and Aggregation : Rows are grouped based on a column value. Aggregation functions like sum, avg, min, and max are applied to the grouped data.Multiple threads handle different groups in parallel
4. CSV Handling : Reads data line-by-line and stores it in memory. Automatically handles missing values.Supports exporting the final data back to a CSV file.
5. Benchmarking : Time is recorded before and after each operation. Execution time is printed in milliseconds for performance comparison.

Report 1 : https://drive.google.com/file/d/1XIONzLkKPEvMuh7AA4NzRu1Ie1XwTjuv/view?usp=sharing

Report 2 : https://drive.google.com/file/d/1CLxZClsCezTh-vfOaoOTInuIXh50n1lU/view?usp=sharing

Video :  https://drive.google.com/file/d/1MQz0iIkl0bGWBdwnPpThPDWtXF8fRUbK/view?usp=sharing
