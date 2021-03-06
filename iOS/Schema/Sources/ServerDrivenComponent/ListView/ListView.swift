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

public struct ListView: RawComponent, AutoInitiableAndDecodable {
    
    // MARK: - Public Properties
    
    public let children: [RawComponent]
    public var direction: Direction = .vertical

// sourcery:inline:auto:ListView.Init
    public init(
        children: [RawComponent],
        direction: Direction = .vertical
    ) {
        self.children = children
        self.direction = direction
    }
// sourcery:end
    
    public init(
        direction: Direction = .vertical,
        @ChildBuilder
        _ children: () -> RawComponent
    ) {
        self.init(children: [children()], direction: direction)
    }
    
    public init(
        direction: Direction = .vertical,
        @ChildrenBuilder
        _ children: () -> [RawComponent]
    ) {
        self.init(children: children(), direction: direction)
    }
}

extension ListView {
    public enum Direction: String, Decodable {
           
        case vertical = "VERTICAL"
        case horizontal = "HORIZONTAL"
    }
}
