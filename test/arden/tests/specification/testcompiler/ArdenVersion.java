package arden.tests.specification.testcompiler;

/** Allows comparing of Arden Syntax versions */
public enum ArdenVersion implements Comparable<ArdenVersion> {
	V1(1, 0), // ASTM E1460-1992
	V2(2, 0),
	V2_1(2, 1),
	V2_5(2, 5),
	V2_6(2, 6),
	V2_7(2, 7),
	V2_8(2, 8),
	V2_9(2, 9),
	V2_10(2, 10);

	public final int major;
	public final int minor;
	public final int rev;

	private ArdenVersion(int major, int minor) {
		this(major, minor, 0);
	}

	private ArdenVersion(int major, int minor, int rev) {
		this.major = major;
		this.minor = minor;
		this.rev = rev;
	}

	@Override
	public String toString() {
		// e.g. "Version 2.5"
		StringBuilder versionBuilder = new StringBuilder("Version ");
		versionBuilder.append(major);
		if (minor != 0) {
			versionBuilder.append('.');
			versionBuilder.append(minor);
		}
		if (rev != 0) {
			versionBuilder.append('.');
			versionBuilder.append(rev);
		}
		return versionBuilder.toString();
	}
}
