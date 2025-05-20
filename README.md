# LZW-PDF-Compression

This is a Java program that compresses a book using the **LZW (Lempel-Ziv-Welch)** algorithm and compares the file size of the compressed file with other common formats (ZIP, DJVU, TXT, etc.).

## 📚 Book Options

There are two test folders included:

- `CatcherTest/` – Contains inputs for the **text-only** PDF: *The Catcher in the Rye*
- `PrinceTest/` – Contains inputs for the **image-heavy** PDF: *The Little Prince*

## ▶️ How to Run

1. Choose one of the two books to test.
2. Copy all the files from the chosen folder (`CatcherTest/` or `PrinceTest/`) and paste them directly into the **root folder** of the project (`TOR3-Project/`).
3. Open the project in your favorite Java IDE.
4. Run the program from `LZWCompressorApp.java`.

## 🛠 Features

- Compresses PDF files using LZW algorithm with a dictionary size of 2²⁰.
- Outputs the compression ratio.
- Compares compressed file size with other formats like:
  - ZIP
  - DJVU
  - TXT
  - HTML
  - RTF

## ✅ Results Summary

- LZW achieved **55.18%** compression on *The Catcher in the Rye* (text-heavy).
- LZW achieved **58.99%** compression on *The Little Prince* (image-heavy).
- Lossless and efficient for textual data.

## 🎉 Have fun!
