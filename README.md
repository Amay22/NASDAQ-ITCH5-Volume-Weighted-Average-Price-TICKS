# NASDAQ-ITCH5-Volume-Weighted-Average-Price-TICKS
Parses NASDAQ ITCH 5 data and Calculates Volume Weighted average Price of all the Stock. 

Just provide the path of the NASDAQ ITCH 5.0 format file to FileInputStream("file-path\\file-name");

Note: path should look something like this "C:\\Users\\Amay\\Documents\\NetBeansProjects\\06022014.NASDAQ_ITCH50" with backslashes

It's a huge dataset like 6.5 GB huge so the program takes up a lot of resources. I have 8GB of RAM and it took 3.5 mins to build and execute so it'll take up a lot of time don't shut it off midway thinking that it's not running. No need to change anything much do anything the program will run and output the file directly in an HTML file.


Takes a lot of time to run the stock folder contains some outputs. It took 25 mins to run on my laptop and it gave out 6883 files i.e. one for each stock. Checkout the output in stocks folder I have attached some of the output.

# NASDAQ ITCH 5 Data 

I used this link for the NASDAQ ITCH 5.0 data

ftp://emi.nasdaq.com/ITCH/06022014.NASDAQ_ITCH50.gz

This link may expire after som days so just change the date om the filename to trading weekday.

# Volume Weighted Average Price for ITCH Ticks

I can't explain it any better than 

http://stockcharts.com/school/doku.php?id=chart_school:technical_indicators:vwap_tick_data

http://www.investopedia.com/terms/v/vwap.asp

# NOTE: It's complete but I haven't implemented broken trade messages. Too lazy for that now. Will do after Semester ends.
