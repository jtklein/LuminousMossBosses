//
//  ObsViewController.h
//  DeveloperBuild
//
//  Created by Jacob Rail on 1/12/15.
//  Copyright (c) 2015 CU Boulder. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "NewObs.h"

@interface ObsViewController : UIViewController <UITableViewDelegate, UITableViewDataSource>

@property (nonatomic, strong) NSMutableArray * myObservations;
@property (nonatomic, strong) NSMutableArray * observationsArray;
@property (nonatomic, strong) NSMutableArray * imageArray;
@property (nonatomic, strong) NSMutableArray * json;


@end