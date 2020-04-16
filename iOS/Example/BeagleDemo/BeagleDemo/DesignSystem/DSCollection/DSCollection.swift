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

import UIKit
import BeagleUI

struct DSCollectionDataSource : Decodable{
    
    struct Card : Decodable {
        let name: String
        let age: Int
    }
    
    let cards: [Card]
}

struct DSCollection: Widget {
    var id: String?
    var appearance: Appearance?
    var flex: Flex?
    var accessibility: Accessibility?
    let dataSource: DSCollectionDataSource

    init(
        appearance: Appearance? = nil,
        flex: Flex? = nil,
        accessibility: Accessibility? = nil,
        dataSource: DSCollectionDataSource
    ) {
        self.appearance = appearance
        self.flex = flex
        self.accessibility = accessibility
        self.dataSource = dataSource
    }
}

extension DSCollection: Renderable {
    func toView(context: BeagleContext, dependencies: RenderableDependencies) -> UIView {
        let view = DSCollectionUIComponent(dataSource: dataSource)
        view.flex.setup(flex)
        return view
    }
}
