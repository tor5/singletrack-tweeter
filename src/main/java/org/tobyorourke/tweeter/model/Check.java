package org.tobyorourke.tweeter.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Check {
	private long lastCheck;
	private final String _id;
	private String _rev;
}
