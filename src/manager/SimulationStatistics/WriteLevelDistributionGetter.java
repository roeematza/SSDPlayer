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
package manager.SimulationStatistics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import manager.ReusableVisualizationSSDManager;
import ui.GeneralStatisticsGraph;
import ui.StatisticsGraph;
import entities.StatisticsColumn;
import entities.reusable_visualization.VisualizationBlock;
import entities.reusable_visualization.VisualizationChip;
import entities.reusable_visualization.VisualizationDevice;
import entities.reusable_visualization.VisualizationPage;
import entities.reusable_visualization.VisualizationPlane;

public class WriteLevelDistributionGetter extends SimulationStatisticsGetter {

	public WriteLevelDistributionGetter(ReusableVisualizationSSDManager manager) {
		super(manager);
	}
	
	@Override
	public int getNumberOfColumns() {
		return 2;
	}

	@Override
	List<StatisticsColumn> getSimulationStatistics(VisualizationDevice device) {
		int[] counters = new int[getNumberOfColumns()];
		Map<Integer, Set<Integer>> utilitySet = new HashMap<Integer, Set<Integer>>();

		for (VisualizationChip chip: device.getChips()) {
			for (VisualizationPlane plane : chip.getPlanes()) {
				for (VisualizationBlock block : plane.getBlocks()) {
					for (int i = 0; i < counters.length; i++) {
						for (VisualizationPage page : block.getPages()) {
							if (page.isValid()) {			
								Set<Integer> writeLevelLPSet = utilitySet.get(page.getWriteLevel());
								if (writeLevelLPSet == null) {
									writeLevelLPSet = new HashSet<Integer>();
									utilitySet.put(page.getWriteLevel(), writeLevelLPSet);
								}
								if(!writeLevelLPSet.contains(page.getLp())) {
									counters[page.getWriteLevel()-1]++;
									writeLevelLPSet.add(page.getLp());
								}
							}
						}
					}
				}
			}
		}
		
		int overallValid = 0;
		for (int i = 0; i < counters.length; i++) {
			overallValid += counters[i];
		}
		
		List<StatisticsColumn> list = new ArrayList<StatisticsColumn>();
		if (overallValid > 0) {					
			for (int i = 0; i < counters.length; i++) {
				list.add(new StatisticsColumn((i+1) + "", ((double)counters[i]*100)/overallValid));
			}
		}
		return list;
	}

	@Override
	public Entry<String, String> getInfoEntry(Device<?, ?, ?, ?> device) {
		return null;
	}

	@Override
	public GeneralStatisticsGraph getStatisticsGraph() {
		return new StatisticsGraph("W.L.Dist.", this);
	}
}