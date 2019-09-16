from __future__ import absolute_import
from __future__ import division
from __future__ import print_function
import torch
import torchvision
from torchvision import datasets, transforms
import matplotlib.pyplot as plt 
import numpy as np
import os
from PIL import Image

import argparse
import torch.nn as nn
import time

from cnn_model import CNN

# Data augmentation and normalization for training 
# Just normalization for validation
data_transforms = {
    'train': transforms.Compose([
#         transforms.RandomSizedCrop(512), # 224 before，544 is good
        transforms.CenterCrop(512),
        transforms.RandomHorizontalFlip(),
        transforms.ToTensor(),
        transforms.Normalize([0.485, 0.456, 0.406], [0.229, 0.224, 0.225])
    ]),
    'val': transforms.Compose([
#         transforms.Scale(256),
        transforms.CenterCrop(512), # 224 before
        transforms.ToTensor(),
        transforms.Normalize([0.485, 0.456, 0.406], [0.229, 0.224, 0.225])
    ]),
}

# Default constants
LEARNING_RATE_DEFAULT = 1e-4
BATCH_SIZE_DEFAULT = 10
MAX_EPOCHS_DEFAULT = 1
EVAL_FREQ_DEFAULT = 1
OPTIMIZER_DEFAULT = 'ADAM'

data_dir = './Data/'

train_sets = datasets.ImageFolder(os.path.join(data_dir, 'train'), data_transforms['train'])
train_loader = torch.utils.data.DataLoader(train_sets, batch_size=10, shuffle=True, num_workers=4)
train_size = len(train_sets)
train_classes = train_sets.classes

val_sets = datasets.ImageFolder(os.path.join(data_dir, 'val'), data_transforms['val'])
val_loader = torch.utils.data.DataLoader(val_sets, batch_size=10, shuffle=False, num_workers=4)
val_size = len(val_sets)

inputs, classes = next(iter(train_loader))

# Visualize a few images
def imshow(inp, title=None):
    """Imshow for Tensor."""
    inp = inp.numpy().transpose((1, 2, 0))
    mean = np.array([0.485, 0.456, 0.406])
    std = np.array([0.229, 0.224, 0.225])
    inp = std * inp + mean
    plt.imshow(inp)
    if title is not None:
        plt.title(title)

        
# Make a grid from batch
out = torchvision.utils.make_grid(inputs, nrow=5)

imshow(out, title=[train_classes[x] for x in classes])

inputs, classes = next(iter(val_loader))

# Make a grid from batch
out = torchvision.utils.make_grid(inputs, nrow=5)

imshow(out, title=[train_classes[x] for x in classes])


def accuracy(outputs, batch_y):
    """
    Computes the prediction accuracy, i.e., the average of correct predictions
    of the network.
    Args:
        predictions: 2D float array of size [number_of_data_samples, n_classes]
        labels: 2D int array of size [number_of_data_samples, n_classes] with one-hot encoding of ground-truth labels
    Returns:
        accuracy: scalar float, the accuracy of predictions.
    """

    _, predicted = torch.max(outputs.data, 1)
    total = batch_y.size(0)
    correct = (predicted == batch_y).sum().item()
    accuracy = 100.0 * correct / total
    
    
    return accuracy

def train(trainloader):
    """
    Performs training and evaluation of CNN model.
    NOTE: You should the model on the whole test set each eval_freq iterations.
    """
    # YOUR TRAINING CODE GOES HERE
    n_channels = 3
    n_classes = 5
    
    cnn = CNN(n_channels, n_classes)
    
    device = torch.device("cuda:0" if torch.cuda.is_available() else "cpu")
    
    cnn.to(device)
    
    # Loss and Optimizer
    criterion = nn.CrossEntropyLoss() 
    optimizer = torch.optim.Adam(cnn.parameters(), lr=LEARNING_RATE_DEFAULT)
    
    losses = []
    accuracies = []
    
    for epoch in range(MAX_EPOCHS_DEFAULT):
        timestart = time.time()
        running_loss = 0.0
        for step, (batch_x, batch_y) in enumerate(trainloader):
            
            # zero the parameter gradients
            optimizer.zero_grad()
            
            # Forward + Backward + Optimize
            
            batch_x, batch_y = batch_x.to(device), batch_y.to(device)
            
            outputs = cnn(batch_x)
            loss = criterion(outputs, batch_y)
            loss.backward()
            optimizer.step()
            
            running_loss += loss.item()
            
            if step % EVAL_FREQ_DEFAULT == EVAL_FREQ_DEFAULT-1:  
                print('[epoch: %d, step: %5d] loss: %.4f' %
                          (epoch, step, running_loss / EVAL_FREQ_DEFAULT))
                losses.append(running_loss / EVAL_FREQ_DEFAULT)
                running_loss = 0.0
                accu = accuracy(outputs, batch_y)
                accuracies.append(accu)
                print('Accuracy on the %d train images: %.3f %%' % (batch_y.size(0),
                            accu))
            
        break
        
        print('epoch %d cost %3f sec' %(epoch,time.time()-timestart))
    
    print('---Finished Training---')
    
    return cnn, losses, accuracies


cnn, losses, accuracies = train(train_loader)
