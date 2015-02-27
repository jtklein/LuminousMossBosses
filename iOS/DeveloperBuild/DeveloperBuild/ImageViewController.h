//
//  ImageViewController.h
//  DeveloperBuild
//
//  Created by Jacob Rail on 2/24/15.
//  Copyright (c) 2015 CU Boulder. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface ImageViewController : UIViewController <UIImagePickerControllerDelegate, UINavigationControllerDelegate>{
	UIImageView * imageView;
	UIButton * choosePhotoBtn;
	UIButton * takePhotoBtn;
}

/* in old tutorial for getting camera to work. */
//@property (strong, nonatomic) IBOutlet UIImageView *imageView;
//- (IBAction)takePhoto:(UIButton *)sender;
//- (IBAction)selectPhoto:(UIButton *)sender;

@property (nonatomic, retain) IBOutlet UIImageView * imageView;
@property (nonatomic, retain) IBOutlet UIButton * choosePhotoBtn;
@property (nonatomic, retain) IBOutlet UIButton * takePhotoBtn;

- (IBAction)getPhoto:(id) sender;

@end