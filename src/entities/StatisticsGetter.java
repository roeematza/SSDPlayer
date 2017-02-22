/*******************************************************************************
 * SSDPlayer Visualization Platform (Version 1.0)
 * Authors: Roman Shor, Gala Yadgar, Eitan Yaakobi, Assaf Schuster
 * Copyright (c) 2015, Technion � Israel Institute of Technology
 * All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that
 * the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following
 * disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
 * following disclaimer in the documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS 
 * OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY
 * AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. 
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, 
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, 
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) 
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE
 *******************************************************************************/
package entities;

import java.util.List;
import java.util.Map.Entry;

import ui.GeneralStatisticsGraph;


/**
 * @author Roman
 * 
 * This interface that represents a statistics gathered by SSD manager.
 * Every gathered statistics in this simulator implements this interface.
 */
public interface StatisticsGetter {
	/**
	 * The Graphs cannot change dynamically number of the columns or lines, 
	 * they are presenting. For that reason the statistics getter must 
	 * explicitly state number of gathered columns or lines. 
	 * @return number of gathered columns or lines
	 */
	int getNumberOfColumns();
	/**
	 * This is the method in which the statistics getter calculates the statistics,
	 * for the current Device state.
	 * @param device - to calculate the statistics on
	 * @return list of statistics columns for the current Device's state.  
	 */
	List<StatisticsColumn> getStatistics(Device<?,?,?,?> device);
	GeneralStatisticsGraph getStatisticsGraph();
	Entry<String, String> getInfoEntry(Device<?, ?, ?, ?> paramDevice);

}
