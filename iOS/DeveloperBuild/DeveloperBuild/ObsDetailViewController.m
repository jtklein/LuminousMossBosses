//
//  ObsDetailViewController.m
//  DeveloperBuild
//
//  Created by Jacob Rail on 3/14/15.
//  Copyright (c) 2015 CU Boulder. All rights reserved.
//

#import "ObsDetailViewController.h"
#import "detectionHelper.h"
#import "UserDataDatabase.h"
#import "IdentifyingAssets.h"
#import <AssetsLibrary/AssetsLibrary.h>

detectionHelper *detectionObject;

@interface ObsDetailViewController ()
@end

@implementation ObsDetailViewController
@synthesize nameLabel;
@synthesize percentLabel;
@synthesize dateLabel;
@synthesize obsImage;
@synthesize plantInfo;
@synthesize idButton;
@synthesize longitudeLabel;
@synthesize latitudeLabel;

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
	nameLabel.text = [NSString stringWithFormat:@"Name: %@", [plantInfo objectForKey:@"imghexid"]];
	percentLabel.text = [NSString stringWithFormat:@"%@%% ", [plantInfo objectForKey:@"percentIDed"]];
	dateLabel.text = [NSString stringWithFormat:@"Date: %@", [plantInfo objectForKey:@"datetime"]];
	latitudeLabel.text = [NSString stringWithFormat:@"%@", [plantInfo objectForKey:@"latitude"]];
	longitudeLabel.text = [NSString stringWithFormat:@"%@", [plantInfo objectForKey:@"longitude"]];

	
	
	NSURL *url = [NSURL URLWithString:[plantInfo objectForKey:@"imghexid"]];

	if (![url  isEqual: @"(null)"]) {
		ALAssetsLibrary *lib = [[ALAssetsLibrary alloc] init];
		
		[lib assetForURL: url resultBlock: ^(ALAsset *asset) {
			ALAssetRepresentation *r = [asset defaultRepresentation];
            UIImageOrientation orientation = (UIImageOrientation) (int) r.orientation;
			obsImage.image = [UIImage imageWithCGImage:r.fullResolutionImage scale:r.scale orientation:orientation];
            
		}
			failureBlock: nil];
	}
	// if this came from the identified tab
	if ([[plantInfo objectForKey:@"status"] isEqual:@"pending-noid"]){
        NSString* imghexid = [plantInfo objectForKey:@"imghexid"];
        detectionObject = [IdentifyingAssets getByimghexid:imghexid];
		if ([detectionObject.percentageComplete isEqualToNumber:[NSNumber numberWithInt:0]]){
			[self.idButton setEnabled:NO];
			[self.activityIndicator startAnimating];
		}
	}
	else {
		[idButton setHidden:YES];
		[idButton setEnabled:NO];
	}
}

- (void)viewWillAppear:(BOOL)animated{
	// add an observer to the identification helper object assosiated with the asset id
	[[IdentifyingAssets getByimghexid:[plantInfo objectForKey:@"imghexid"]] addObserver:self forKeyPath:@"percentageComplete" options:NSKeyValueObservingOptionNew | NSKeyValueObservingOptionOld context:nil];
}

- (void)viewWillDisappear:(BOOL)animated{
	// remove the observer assosiated with the asset id.
	[[IdentifyingAssets getByimghexid:[plantInfo objectForKey:@"imghexid"]] removeObserver:self forKeyPath:@"percentageComplete"];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)updateObservationData{
	// set everthing we just calculated
	obsImage.image = [detectionObject identifiedImage];
	percentLabel.text = [NSString stringWithFormat:@"%.0f%%",[[detectionObject probability] floatValue]*100];
	
	if ([detectionObject positiveID]) {
		percentLabel.textColor = [UIColor colorWithRed:0 green:255.f blue:0 alpha:1];
	} else {
		percentLabel.textColor = [UIColor colorWithRed:255.f green:0 blue:0 alpha:1];
	}
	
	idButton.hidden = YES;
}

- (IBAction)startIdentificationButton:(UIButton *)sender {
	//Start an activity indicator here
	[self.activityIndicator startAnimating];
	//disable id button
	[idButton setEnabled:NO];
	
	// run the detection algorithm.
	dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{
		[detectionObject runDetectionAlgorithm:obsImage.image];
	});
}

-(void)observeValueForKeyPath:(NSString *)keyPath ofObject:(id)object change:(NSDictionary *)change context:(void *)context{
	
	if ([keyPath isEqualToString:@"percentageComplete"]) {
		
		if ([[change valueForKey:@"new"] isEqualToNumber:[NSNumber numberWithInt:1]]) {
			dispatch_sync(dispatch_get_main_queue(), ^{
				[self updateObservationData];	// update the on screen data
				[[self activityIndicator] stopAnimating];	// stop the activity indicator
				[[self idButton] setHidden:YES];	// hide the id button.
			});
		}
        
        // Changing back to -1 indicates an error with updating the database after identification.
        // Want to reset back to before identification was started.
        if ([[change valueForKey:@"new"] isEqualToNumber:[NSNumber numberWithInt:-1]]) {
            dispatch_sync(dispatch_get_main_queue(), ^{
                [[self activityIndicator] stopAnimating];
            });
        }
	}
}

#pragma mark - Navigation

/*
// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
	if ([segue.identifier isEqualToString:@"MyObsSegue"]) {
		NSIndexPath *indexPath = [self.tableView indexPathForSelectedRow];
	}
}
*/

@end
