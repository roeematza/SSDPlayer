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
package entities.reusable;

import entities.Device;
import entities.EntityInfo;


public class ReusableDevice extends Device<ReusablePage, ReusableBlock, ReusablePlane, ReusableChip> {
	public static class Builder extends Device.Builder<ReusablePage, ReusableBlock, ReusablePlane, ReusableChip> {
		private ReusableDevice device;		

		public Builder() {
			setDevice(new ReusableDevice());
		}
		
		public Builder(ReusableDevice device) {
			setDevice(new ReusableDevice(device));
		}
		
		@Override
		public ReusableDevice build() {
			validate();
			return new ReusableDevice(device);
		}
		
		protected void setDevice(ReusableDevice device) {
			super.setDevice(device);
			this.device = device;
		}
	}
	
	protected ReusableDevice() {}
	
	protected ReusableDevice(ReusableDevice other) {
		super(other);
	}

	@Override
	public Builder getSelfBuilder() {
		return new Builder(this);
	}

	public EntityInfo getInfo() {
		EntityInfo result = super.getInfo();

		result.add("Recycled blocks", Integer.toString(getNumOfRecycledBlocks()), 4);
		return result;
	}

	private int getNumOfRecycledBlocks() {
		int recycledBlocks = 0;
		for (ReusableChip chip : getChips()) {
			recycledBlocks += chip.getNumOfRecycledBlocks();
		}
		return recycledBlocks;
	}

	public int getGCExecutions() {
		int gcExecutions = 0;
		for (ReusableChip chip : getChips()) {
			gcExecutions += chip.getGCExecutions();
		}
		return gcExecutions;
	}
}
