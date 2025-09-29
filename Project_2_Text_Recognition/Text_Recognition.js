
const fs = require("fs");
const path = require("path");
const sharp = require("sharp");
const vision = require("@google-cloud/vision");

const CREDENTIALS = JSON.parse(JSON.stringify({}));

const visionClient = new vision.ImageAnnotatorClient({
  credentials: CREDENTIALS
});

// Directory to watch
const WATCH_DIR = "./input_images";

//Preprocess image 
async function preprocessImage(inputPath) {
  const image = await sharp(inputPath)
    .resize(1024)
    .grayscale()
    .threshold(180)
    .toBuffer();
  return image;
}

// Run text detection on an image
async function detectText(pImage) {
  const [result] = await visionClient.documentTextDetection({image: { content: pImage }});
  const detections = result.fullTextAnnotation ? result.fullTextAnnotation.text : "";
  return detections.trim();
}

// Get most recent .png file in a folder
function getLatestPng(dirPath) {
  const files = fs.readdirSync(dirPath)
    .filter(f => f.endsWith(".png"))
    .map(f => ({
      name: f,
      time: fs.statSync(path.join(dirPath, f)).mtime.getTime()
    }))
    .sort((a, b) => b.time - a.time);

  return files.length > 0 ? path.join(dirPath, files[0].name) : null;
}

(async function mainLoop() {
  console.log("waiting...");

  let lastProcessed = null;

  while (true) {
    try {
      const latestFile = getLatestPng(WATCH_DIR);

      if (latestFile !== lastProcessed) {
        console.log(`\nnew file: ${latestFile}`);

        const pImage = await preprocessImage(latestFile);

        const text = await detectText(pImage);
        console.log("Extracted Text:");
        console.log(text || "No handwriting detected");

        lastProcessed = latestFile;
      }
    } catch (err) {
      console.error("Error:", err.message);
    }

    // Wait 1 seconds before checking again
    await new Promise(res => setTimeout(res, 1000));
  }
})();