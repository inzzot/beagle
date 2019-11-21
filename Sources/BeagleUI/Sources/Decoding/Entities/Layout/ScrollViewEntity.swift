//
//  ScrollViewEntity.swift
//  BeagleUI
//
//  Created by Tarcisio Clemente on 06/11/19.
//  Copyright © 2019 Daniel Tes. All rights reserved.
//

import Foundation

/// Defines an API representation for `ScrollView`
struct ScrollViewEntity: WidgetEntity {
    
    let children: [WidgetConvertibleEntity]
    let reversed: Bool
    
    private let childrenContainer: [WidgetEntityContainer]
    
    private enum CodingKeys: String, CodingKey {
        case childrenContainer = "children"
        case reversed
    }
    
    init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        try self.init(
            childrenContainer: container.decode([WidgetEntityContainer].self, forKey: .childrenContainer),
            reversed: container.decodeIfPresent(Bool.self, forKey: .reversed)
        )
    }
    
    init(
        childrenContainer: [WidgetEntityContainer],
        reversed: Bool?
    ) {
        self.childrenContainer = childrenContainer
        children = childrenContainer.compactMap { $0.content }
        self.reversed = reversed ?? false
    }
    
}
extension ScrollViewEntity: WidgetConvertible, ChildrenWidgetMapping {

    func mapToWidget() throws -> Widget {

        let children = try mapChildren()

        return ScrollView(
            children: children,
            reversed: reversed
        )
        
    }

}
