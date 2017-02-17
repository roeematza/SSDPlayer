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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import manager.ReusableSSDManager;
import ui.GeneralStatisticsGraph;
import ui.RegularHistoryGraph;
import entities.StatisticsColumn;
import entities.reusable.ReusableBlock;
import entities.reusable.ReusableChip;
import entities.reusable.ReusableDevice;
import entities.reusable.ReusablePage;
import entities.reusable.ReusablePlane;

public class WritesToGCGetter extends SecondWritesStatisticsGetter {

	public WritesToGCGetter(ReusableSSDManager manager) {
		super(manager);
	}

	@Override
	public int getNumberOfColumns() {
		return 2;
	}

	@Override
	List<StatisticsColumn> getSecondWriteStatistics(ReusableDevice device) {
		int[] validW = new int[2];
		int[] validGC = new int[2];
		
		for (ReusableChip chip : device.getChips()) {
			for (ReusablePlane plane : chip.getPlanes()) {				
				for (ReusableBlock block : plane.getBlocks()) {
					Set<Integer> utilitySet = new HashSet<Integer>();
					for (ReusablePage page : block.getPages()) {
						if (page.isValid() && (!utilitySet.contains(page.getLp()))) {
							utilitySet.add(page.getLp());
							if (page.isGC()) {
								validGC[page.getGcWriteLevel()-1]++;
							} else {
								validW[page.getWriteLevel()-1]++;
							}
						}
					}
				}
			}
		}
		
		List<StatisticsColumn> list = new ArrayList<StatisticsColumn>();
		for (int i = 0; i < validW.length; i++) {
			int total = validW[i] + validGC[i];
			list.add(new StatisticsColumn("write "+(i+1), 
					total==0 ? 0 : ((double)validW[i]*100)/total,
					i%2==0, manager.getWriteLevelColor(i+1)));
		}
		return list;
	}
	
	@Override
	public Entry<String, String> getInfoEntry(Device<?, ?, ?, ?> device) {
		return null;
	}

	@Override
	public GeneralStatisticsGraph getStatisticsGraph() {
		return new RegularHistoryGraph("Writes to GC(%)", this, 100, 0);
	}
}