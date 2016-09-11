package net.mightyelemental.tree;

import java.util.ArrayList;
import java.util.List;

public class TreeGenerator {
	
	
	public static List<Branch> generateBranches(Branch startPoint, int iteration) {
		List<Branch> lines = new ArrayList<Branch>();
		float angleOfStart = MathHelper.getAngle(startPoint);
		int max = TreeGen.mainBranches - (iteration);
		if (max <= 0) {
			max = TreeGen.rand.nextInt(1) + 1;
		}
		for (int i = 0; i < max; i++) {
			// float length = (startPoint.length() * ((TreeGen.rand.nextInt(10) + (50 + (iteration * 10))) / 100f));
			float length = startPoint.length() / MathHelper.goldenRatio();
			// length *= Math.abs((TreeGen.rand.nextInt(200) + 900) / 1000f);
			float angle = angleOfStart + (TreeGen.rand.nextInt(95 * 2) - 95);
			
			float endX = length * ((float) Math.cos(Math.toRadians(angle - 180))) + startPoint.getEnd().x;
			float endY = length * ((float) Math.sin(Math.toRadians(angle - 180))) + startPoint.getEnd().y;
			Branch l = new Branch(startPoint.getEnd().x, startPoint.getEnd().y, endX, endY);
			l.setParent(startPoint);
			lines.add(l);
		}
		
		return lines;
	}
	
	public static List<List<Branch>> generateTree(int startX, int startY) {
		List<List<Branch>> lines = new ArrayList<List<Branch>>();
		List<Branch> start = new ArrayList<Branch>();
		start.add(new Branch(startX, startY, startX + (TreeGen.rand.nextInt(40) - 20), startY - (TreeGen.rand.nextInt(50) + 150)));
		lines.add(start);
		System.out.println(MathHelper.getAngle(start.get(0)));
		
		for (int i = 0; i < TreeGen.iterations; i++) {
			List<Branch> lin = new ArrayList<Branch>();
			for (int x = 0; x < lines.get(i).size(); x++) {
				lin.addAll(generateBranches(lines.get(i).get(x), i));
			}
			lines.add(lin);
		}
		
		return lines;
	}
	
}
