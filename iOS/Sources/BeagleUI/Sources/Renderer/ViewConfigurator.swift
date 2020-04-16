/*
 * Copyright 2020 ZUP IT SERVICOS EM TECNOLOGIA E INOVACAO SA
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import Foundation
import UIKit

public protocol ViewConfiguratorProtocol: AnyObject {
    var view: UIView? { get set }

    func setup(_ widget: Widget)
    func setup(appearance: Appearance?)
    func setup(id: String?)
    func setup(accessibility: Accessibility?)
}

public protocol DependencyViewConfigurator {
    var viewConfigurator: (UIView) -> ViewConfiguratorProtocol { get }
}

public extension UIView {

    var beagle: ViewConfiguratorProtocol {
        return Beagle.dependencies.viewConfigurator(self)
    }
}

class ViewConfigurator: ViewConfiguratorProtocol {

    weak var view: UIView?

    init(view: UIView) {
        self.view = view
    }

    func setup(_ widget: Widget) {
        setup(appearance: widget.appearance)
        setup(id: widget.id)
        setup(accessibility: widget.accessibility)
        view?.flex.setup(widget.flex)
    }

    func setup(appearance: Appearance?) {
        if let hex = appearance?.backgroundColor {
            view?.backgroundColor = .init(hex: hex)
        }
        if let cornerRadius = appearance?.cornerRadius {
            view?.layer.masksToBounds = true
            view?.layer.cornerRadius = CGFloat(cornerRadius.radius)
        }
    }

    func setup(id: String?) {
        if let id = id {
            view?.accessibilityIdentifier = id
        }
    }

    func setup(accessibility: Accessibility?) {
        ViewConfigurator.applyAccessibility(accessibility, to: view)
    }
    
    static func applyAccessibility(_ accessibility: Accessibility?, to object: NSObject?) {
        guard let accessibility = accessibility else { return }
        if let label = accessibility.accessibilityLabel {
            object?.accessibilityLabel = label
        }
        object?.isAccessibilityElement = accessibility.accessible
    }
}
