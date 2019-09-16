from __future__ import absolute_import
from __future__ import division
from __future__ import print_function
import torch
import torchvision
from torchvision import transforms
import numpy as np
from PIL import Image
import argparse
import torch.nn as nn
import sys

from cnn_model import CNN

IMAGEPATH = sys.argv[1]  # './testImages/test_unk.jpg'

MODELPATH = '/Users/victor/Github/main-project-repository-hello-sustech/TrainingModel/SCRIPT/Train.ckpt'

DROPSIZE = 512

trans=transforms.Compose(
    [
        transforms.CenterCrop(DROPSIZE),  #512
        transforms.ToTensor(),
        transforms.Normalize([0.485, 0.456, 0.406], [0.229, 0.224, 0.225])

    ])

def main():


    img = Image.open(IMAGEPATH)
    transed_img=trans(img)

    n_channels = 3
    n_classes = 5
        
    test_model = CNN(n_channels, n_classes)

    model_dict=test_model.load_state_dict(torch.load(MODELPATH))

    transed_img = transed_img.view(1,3,DROPSIZE,DROPSIZE)

    outputs = test_model(transed_img)

    _, predicted = torch.max(outputs.data, 1)

    # loss:
    criterion = nn.CrossEntropyLoss() 
    loss = criterion(outputs, torch.from_numpy(np.array([2])) )

    # if loss >= 1:
    #     print("Unknown")
    #     return 
    if predicted == 0:
        print("Dorm")
    if predicted == 1:
        print("Gym")
    if predicted == 2:
        print("Library")
    if predicted == 3:
        print("Market")
    if predicted == 4:
        print("Teaching Building 1")


if __name__ == '__main__':
    main()