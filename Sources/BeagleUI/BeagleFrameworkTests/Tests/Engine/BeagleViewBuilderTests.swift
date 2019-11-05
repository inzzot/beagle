//
//  BeagleViewBuilderTests.swift
//  BeagleFrameworkTests
//
//  Created by Eduardo Sanches Bocato on 04/11/19.
//  Copyright © 2019 Daniel Tes. All rights reserved.
//

import XCTest
@testable import BeagleUI

final class BeagleViewBuilderTests: XCTestCase {
    
    func test_publicInit_shouldSetupDependenciesProperly() {
        // Given
        let sut = BeagleViewBuilding()
        
        // When
        let mirror = Mirror(reflecting: sut)
        let rendererProvider = mirror.firstChild(of: WidgetRendererProviding.self)
        let flexConfigurator = mirror.firstChild(of: FlexViewConfigurator.self)
        
        // Then
        XCTAssertNotNil(rendererProvider, "Expected to find `WidgetRendererProviding`, but got nil.")
        XCTAssertNotNil(flexConfigurator, "Expected to find `FlexViewConfigurator`, but got nil.")
    }
    
    func test_buildFromRootWidget_shouldReturnTheExpectedRootView() {
        // Given
        let sut = BeagleViewBuilding()
        let widget = Text("Text")
        
        // When
        let rootView = sut.buildFromRootWidget(widget)
        
        // Then
        XCTAssertTrue(rootView is UILabel, "Expected a `UITextField`, but got \(String(describing: rootView)).")
    }
    
}


