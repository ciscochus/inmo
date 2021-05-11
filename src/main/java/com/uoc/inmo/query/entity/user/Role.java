package com.uoc.inmo.query.entity.user;

public class Role {
    public static final String PROFESIONAL = "PROFESIONAL";
    public static final String PARTICULAR = "PARTICULAR";
    public static final String ADMIN = "ADMIN";

	private Role() {
		// Static methods and fields only
	}

	public static String[] getAllRoles() {
		return new String[] { PROFESIONAL, PARTICULAR, ADMIN };
	}
}
