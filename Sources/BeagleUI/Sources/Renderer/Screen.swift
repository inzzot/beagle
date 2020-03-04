//
//  Copyright © 06/02/20 Zup IT. All rights reserved.
//

import UIKit

public struct Screen {
    
    // MARK: - Public Properties
    
    public let appearance: Appearance?
    public let safeArea: SafeArea?
    public let navigationBar: NavigationBar?
    public let header: ServerDrivenComponent?
    public let content: ServerDrivenComponent
    public let footer: ServerDrivenComponent?
    
    public init(
        appearance: Appearance? = nil,
        safeArea: SafeArea? = nil,
        navigationBar: NavigationBar? = nil,
        header: ServerDrivenComponent? = nil,
        content: ServerDrivenComponent,
        footer: ServerDrivenComponent? = nil
    ) {
        self.safeArea = safeArea
        self.navigationBar = navigationBar
        self.header = header
        self.content = content
        self.footer = footer
        self.appearance = appearance
    }
    
    func toView(context: BeagleContext, dependencies: RenderableDependencies) -> UIView {
        return ScreenComponent(
            appearance: appearance,
            safeArea: safeArea,
            navigationBar: navigationBar,
            header: header,
            content: content,
            footer: footer
        ).toView(context: context, dependencies: dependencies)
    }
}
