import numpy as np
 import pandas as pd
 import random
 from sklearn.model_selection import train_test_split
 from sklearn.preprocessing import StandardScaler

#Activation functions
 def relu(x):
    return np.maximum(0, x)
 def relu_derivative(x):
    return (x > 0).astype(float)
 #Mean Squared Error
 def mse_loss(y_true, y_pred):
    return np.mean((y_true - y_pred) ** 2)
 #Neural Network
 class NeuralNetwork:

    def __init__(self, input_size, hidden_size, output_size, learning_rate=0.01):
        self.learning_rate = learning_rate
        #Initialize weights and biases
        self.W1 = np.random.randn(input_size, hidden_size) * 0.01
        self.b1 = np.zeros((1, hidden_size))
        self.W2 = np.random.randn(hidden_size, output_size) * 0.01
        self.b2 = np.zeros((1, output_size))

    def forward(self, x):
        self.Z1 = np.dot(x, self.W1) + self.b1 #weighted sum of input
        self.A1 = relu(self.Z1) # activation of the hidden layer
        self.Z2 = np.dot(self.A1, self.W2) + self.b2 #raw output of the neural network
        return self.Z2

    def backpropagation(self, x, y, output):
        m = y.shape[0]
        #Compute gradients
        dZ2 = output - y #error in output layer
        dW2 = np.dot(self.A1.T, dZ2) / m
        db2 = np.sum(dZ2, axis=0, keepdims=True) / m
        dA1 = np.dot(dZ2, self.W2.T)
        dZ1 = dA1 * relu_derivative(self.Z1) #error in input layer
        dW1 = np.dot(x.T, dZ1) / m
        db1 = np.sum(dZ1, axis=0, keepdims=True) / m
        #Update weights
        self.W1 -= self.learning_rate * dW1
        self.b1 -= self.learning_rate * db1
        self.W2 -= self.learning_rate * dW2
        self.b2 -= self.learning_rate * db2

    def train(self, x, y):
        target_loss=0.2
        max_epochs=10000
        epoch = 0
        while True:
            output = self.forward(x)
            loss = mse_loss(y, output)
            self.backpropagation(x, y, output)
            if epoch % 100 == 0:
                print(f"Epoch {epoch}, Loss: {loss:.2f}")
            if loss <= target_loss or epoch >= max_epochs:
                break
            #prints every 100 epochs so it doesnt clog up the screen
            epoch += 1
    
def load_data():
    ds = pd.read_csv('housing.csv')
    #get rid of missing values
    ds = ds.dropna()
    #Convert ocean_proximity to numerical value
    ds = pd.get_dummies(ds, columns=['ocean_proximity'], drop_first=True)
    x = ds.drop('median_house_value', axis=1).values
    y = ds['median_house_value'].values.reshape(-1, 1)
    #Normalize features and target using scalers to transorm the data by changing its scale
    scaler_x = StandardScaler()
    scaler_y = StandardScaler()
    x = scaler_x.fit_transform(x)
    y = scaler_y.fit_transform(y)
    #20% test, 80% train. random seed is 100 to make every run consistent 
    return train_test_split(x, y, test_size=0.2, random_state=100), scaler_x, scaler_y

def predict_house(x_test, y_test, scaler_x, scaler_y):
    # Get feature names from the dataset
    feature_names = list(pd.get_dummies(pd.read_csv('housing.csv'))[1:].columns)
    # Select a random house index
    random_index = random.randint(0, len(x_test) - 1)
    test_house = x_test[random_index].reshape(1, -1)
    # Print feature values with labels
    print("House Features")
    for name, value in zip(feature_names, scaler_x.inverse_transform(test_house)[0]):
        print(f"{name}: {value:.2f}")
    # Predict price
    predicted_price = nn.forward(test_house)
    predicted_price_original = scaler_y.inverse_transform(predicted_price)
    # Print actual vs predicted price
    actual_price = scaler_y.inverse_transform(y_test[random_index].reshape(1, -1))
    print("Predicted Price: $", round(predicted_price_original[0][0], 2))
    print("Actual Price: $", round(actual_price[0][0], 2))

 #get the data
 (x_train, x_test, y_train, y_test), scaler_x, scaler_y = load_data()
 #Initialize and train the network
 nn = NeuralNetwork(input_size=x_train.shape[1], hidden_size=10, output_size=1, learning_rate=0.01)
 nn.train(x_train, y_train)

 # Run example prediction
 predict_house(x_test, y_test, scaler_x, scaler_y)