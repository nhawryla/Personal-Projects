This project was developed as part of a class assignment to design a neural network capable of predicting house prices based on housing data. Using the California Housing Dataset, the model demonstrated the ability to predict prices within a relatively narrow margin of error in test cases.

The network architecture consists of three layers an input layer, a hidden layer, and an output layer. Weight adjustments were performed using backpropagation. The ReLU activation function was selected due to its computational efficiency compared to alternatives such as sigmoid or tanh. Mean squared error (MSE) was employed as the loss function.

For preprocessing, unnecessary columns were removed, and the remaining data was normalized to a scale more suitable for the model. The dataset was then divided into an 80/20 split for training and testing. This ratio was chosen to provide a sufficiently large test set to capture edge cases while maintaining a robust training set for effective model learning.

How to run : 

- install scikit-learn
- download the california housing dataset from kaggle placing it in the same directory
- run script
