//
//  ScreenBrightness.h
//  ScreenBrightness
//
//  Created by Atticus White on 12/7/15.
//  Copyright © 2015 Atticus White. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

#if __has_include(<React/RCTBridgeModule.H>)
#import <React/RCTBridgeModule.h>
#else
#import "RCTBridgeModule.h"
#endif

@interface ScreenBrightness : NSObject<RCTBridgeModule>

@end
