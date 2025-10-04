This test case was developed after the main project to explore additional functionality beyond the original group work. The primary objective was to test the ability to store images locally on the server and configure the script to monitor folder updates, automatically retrieving recent images and extracting their text. Another goal was to evaluate how preprocessing techniques influenced the accuracy of text extraction. Initial testing indicated that preprocessing produced improvements in overall performance.

The preprocessing steps included three key methods:

- Resizing : Adjusting the image dimensions ensured proper scaling, addressing both under- and over-sizing issues. This also enhanced character distinction.

- Grayscale Conversion : Converting the image to grayscale simplified it by removing irrelevant color information, allowing the focus to remain on text features.

- Thresholding : Applying a threshold helped separate darker text from lighter backgrounds, sharpening edges and improving text definition. A threshold value of 180 was selected because it provided an effective cutoff for handwritten text. In most test cases, the images contained dark text on white paper, making this value a practical choice for minimizing background interference while preserving text visibility.