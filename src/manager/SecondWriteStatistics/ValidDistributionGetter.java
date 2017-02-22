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
package manager.SecondWriteStatistics;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import manager.ReusableSSDManager;
import ui.GeneralStatisticsGraph;
import ui.StatisticsGraph;
import entities.Device;
import entities.StatisticsColumn;
import entities.reusable.ReusableBlock;
import entities.reusable.ReusableChip;
import entities.reusable.ReusableDevice;
import entities.reusable.ReusablePage;
import entities.reusable.ReusablePlane;

public class ValidDistributionGetter extends SecondWritesStatisticsGetter {

	private int writeLevel;

	public ValidDistributionGetter(ReusableSSDManager manager, int writeLevel) {
		super(manager);
		this.writeLevel = writeLevel;
	}

	@Override
	public int getNumberOfColumns() {
		return manager.getPagesNum()/writeLevel + 1;
	}

	@Override
	List<StatisticsColumn> getSecondWriteStatistics(ReusableDevice device) {
		int[] counters = new int[getNumberOfColumns()];

		for (ReusableChip chip : device.getChips()) {
			for (ReusablePlane plane : chip.getPlanes()) {
				for (ReusableBlock block : plane.getBlocks()) {
					Set<Integer> utilitySet = new HashSet<Integer>();
					for (ReusablePage page : block.getPages()) {
						if (page.isValid() && page.getWriteLevel() == writeLevel) { 									
							utilitySet.add(page.getLp());
						}
					}
					counters[utilitySet.size()]++;
				}
			}
		}
		
		int overallBlocks = manager.getBlocksNum()*manager.getPlanesNum()*manager.getChipsNum();
		List<StatisticsColumn> list = new ArrayList<StatisticsColumn>();
		for (int i = 0; i < counters.length; i++) {
			list.add(new StatisticsColumn(i+"", ((double)counters[i]*100)/overallBlocks, i%2==0));
		}
		return list;
	}

	@Override
	public GeneralStatisticsGraph getStatisticsGraph() {
		return new StatisticsGraph("Valid "+writeLevel+" Histogram", this);
	}

	@Override
	public Entry<String, String> getInfoEntry(Device<?, ?, ?, ?> device) {
		List<StatisticsColumn> statistics = getStatistics(device);
		StringBuilder sb = new StringBuilder();
		int colCount = 0;
		for (StatisticsColumn col : statistics) {
			sb.append(col.getColumnName());
			sb.append(": ");
			sb.append((int) col.getValue());
			colCount++;
			if (colCount != statistics.size()) {
				if (colCount % 6 == 5) {
					sb.append(",\n");
				} else {
					sb.append(", ");
				}
			}
		}

		return new AbstractMap.SimpleEntry("Valid" + this.writeLevel + " histogram", sb.toString());
	}
}