# Card Inventory App
Card Inventory is an Android app that organizes and maintains a database of a collection of greeting cards. Items can be added into a database by manual input or by a barcode scanner to fetch a record of the item's location in the store aisle or create a new item if the item does not yet exist in the database. All items can be listed and sorted according to the user's preferences, and said list can be exported to a CSV file in the sd card of the Android device.

|Main Menu|Editing View|List View|
|---|---|---|
|<img src="/screenshots/Screenshot_20170818-172958.png" width="250px" height="auto">|<img src="/screenshots/Screenshot_20170818-173101.png" width="250px" height="auto">|<img src="/screenshots/Screenshot_20170818-173050.png" width="250px" height="auto">|

## Motivation
This app was made as a management tool for a local gift shop selling greeting cards on a shelf. Customers who browse the cards collection often don't return the cards back to their original location, leaving the cleanup to the store associates. By keeping track of the location of the items, store associates do not have to memorize the items' original place, and work is made efficient.

Another reason for this app was, of course, to keep track of item quantities. The store manager would often have to memorize which kinds of cards are in low quantities and may run into the error of stocking the wrong cards. Listing the item quanitities and exporting the sorted lists into a CSV file will make it easier for the store manager to know which kinds cards they should restock on.
## What's New
* Exported CSV files can be importaed back in
* Quantity count increased to 12

## Prerequisites
* Android SDK 25 (Minimum SDK 19)
* Android Build Tools v25.3.1
* Android Support Repository

## Getting Started
This project can be build using the "gradle build" feature in Android Studio.

### Scanning Items
After selecting Search & Scan in the main menu, you can choose to enter in a barcode manually, or scan the item using your camera. This app uses the [ZXing Barcode Scanning Library](https://github.com/zxing/zxing). If the app doesn't recognize the inputted barcode, it will ask you to enter information for a new item. If the item does exist in the database, the app wil pull up a simple information page. From there, you can choose to edit the item information or delete the item itself.

|Search & Scan|Manual Input|Item Information|
|---|---|---|
|<img src="/screenshots/Screenshot_20170818-173006.png" width="250px" height="auto">|<img src="/screenshots/Screenshot_20170818-173418.png" width="250px" height="auto">|<img src="/screenshots/Screenshot_20170831-133441.png" width="250px" height="auto">|

### List Inventory
This brings up a list of all items in your card database. By selecting the sort button in the toolbar, you can sort and filter the list to the way you desire. You can view individual item information from the list view by clicking on the list items.

### Exporting Files
You can export the database using the export feature in the context menu in the top right corner of both the List Inventory and Search & Scan. The app by default exports a CSV file directly to the designated Downloads folder of the Android device. Using [OpenCSV](http://opencsv.sourceforge.net/), the CSV file will be listed according to the last sorting query requested, and will be labeled with a timestamp to avoid accidental overwriting.

|See the Context Menu in the corner|
|---|
|<img src="/screenshots/Screenshot_20170818-173111.png" width="250px" height="auto">|
