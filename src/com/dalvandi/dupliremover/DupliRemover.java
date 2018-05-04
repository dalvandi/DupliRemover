package com.dalvandi.dupliremover;

public class DupliRemover {

	public static void main(String[] args) {
		
		String path;// = "H:\\pictures";
		DupliFinder finder = new DupliFinder();

		if(args.length == 1)
		{
			path = args[0];
			finder.run(path, 0, 0);
		}
		else if(args.length == 2)
		{
			path = args[0];
			if(args[1].equals("-r"))
				finder.run(path, 1, 0);
			else if(args[1].equals("-all"))
				finder.run(path, 0, 1);
			else
				finder.run(path, 0, 0);
		}
		else if(args.length == 3)
		{
			path = args[0];
			if(args[1].equals("-r") && args[2].equals("-all"))
				finder.run(path, 1, 1);
			else if(args[2].equals("-r") && args[1].equals("-all"))
				finder.run(path, 1, 1);
			else
				finder.run(path, 0, 0);
		}
		else
		{
			System.out.println("Error: Wrong arguments");
		}
		
	}
	

}
