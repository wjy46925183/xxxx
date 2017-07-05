/*
 *  Android Wheel Control.
 *  https://code.google.com/p/android-wheel/
 *  
 *  Copyright 2011 Yuri Kanivets
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package gongren.com.dlg.cityUtils;

/**
 * Range for visible items.
 */
public class ItemsRange {
	// First item_find_lv number
	private int first;
	
	// Items count
	private int count;

	/**
	 * Default constructor. Creates an empty range
	 */
    public ItemsRange() {
        this(0, 0);
    }
    
	/**
	 * Constructor
	 * @param first the number of first item_find_lv
	 * @param count the count of items
	 */
	public ItemsRange(int first, int count) {
		this.first = first;
		this.count = count;
	}
	
	/**
	 * Gets number of  first item_find_lv
	 * @return the number of the first item_find_lv
	 */
	public int getFirst() {
		return first;
	}
	
	/**
	 * Gets number of last item_find_lv
	 * @return the number of last item_find_lv
	 */
	public int getLast() {
		return getFirst() + getCount() - 1;
	}
	
	/**
	 * Get items count
	 * @return the count of items
	 */
	public int getCount() {
		return count;
	}
	
	/**
	 * Tests whether item_find_lv is contained by range
	 * @param index the item_find_lv number
	 * @return true if item_find_lv is contained
	 */
	public boolean contains(int index) {
		return index >= getFirst() && index <= getLast();
	}
}