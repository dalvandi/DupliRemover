package com.dalvandi.dupliremover;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;

public class DupliFinder {

	private ArrayList<String> extensions = new ArrayList<String>();
	private boolean remove = false;
	private boolean allext = false;
	
	public DupliFinder()
	{
		extensions.add("jpg");
		extensions.add("gif");
		extensions.add("psd");
		extensions.add("pdf");
		extensions.add("doc");
		extensions.add("docx");
		extensions.add("mp3");
		extensions.add("mp4");
		extensions.add("avi");
		extensions.add("wma");
		extensions.add("jpeg");
		extensions.add("png");
	}
	
	public void run(String path, int r, int ext)
	{
		if(r == 1) 
			remove = true;
		if(ext == 1)
			allext = true;
		System.out.println("DupliRemover started ...");
		System.out.println("The process may take some time.");
		File f = new File(path);
		ArrayList<String> foundfilename = new ArrayList<String>();
		ArrayList<String> foundabspath = new ArrayList<String>();
		
		ArrayList<ArrayList<String>> foundfull = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<String>> dups = new ArrayList<ArrayList<String>>();
		
		foundfull.add(foundfilename);
		foundfull.add(foundabspath);
		
		
		
		try {
			dups = findDups(f, foundfull);
			System.out.println("Duplicates found: " + dups.get(0).size());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(remove)
		for(String abs : dups.get(1))
		{
			File ftr = new File(abs);
			ftr.delete();
			System.out.println("Duplicated file " + abs + " deleted.");
		}
	}
	
	
	public ArrayList<ArrayList<String>> findDups(File file, ArrayList<ArrayList<String>> foundfiles) throws Exception
	{
		ArrayList<ArrayList<String>> foundfilesfull = foundfiles;
		ArrayList<String> filesname = foundfilesfull.get(0);
		ArrayList<String> filesabs = foundfilesfull.get(1);
		
		
		ArrayList<ArrayList<String>> dupsfull = new ArrayList<ArrayList<String>>();
		ArrayList<String> dupsname = new ArrayList<String>();
		ArrayList<String> dupsabs = new ArrayList<String>();
		ArrayList<String> dupspar = new ArrayList<String>();
		dupsfull.add(dupsname);
		dupsfull.add(dupsabs);
		dupsfull.add(dupspar);
		
		
		ArrayList<File> files;
		if(file.getName().equals("System Volume Information"))
			files = new ArrayList<File>();
		else
			files = new ArrayList<File>(Arrays.asList(file.listFiles()));


		
		
		//for(File f : files)
		for(int i = files.size() - 1 ; i >= 0; i--)
		{
			File f = files.get(i);
			
			if(f.isFile() && f.length() < 50000000 && (extensions.contains(getFileExtension(f)) || allext))
			{
				ArrayList<Integer> ff = indexOfAll(f.getName(), filesname);
				
				if(!ff.isEmpty()){
					for(int index : ff) {
					File foundfile = new File(filesabs.get(index));
					if(isSame(f, foundfile)) {
						dupsname.add(f.getName());
						dupsabs.add(f.getAbsolutePath());
						dupspar.add(foundfile.getAbsolutePath());
						dupsfull.set(0, dupsname);
						dupsfull.set(1, dupsabs);
						dupsfull.set(2, dupspar);
						System.out.println("Duplicate: " + f.getAbsolutePath() + " | Main: " + foundfile.getAbsolutePath());
						break;
					}
					else
					{
						filesname.add(f.getName());
						filesabs.add(f.getAbsolutePath());
						foundfilesfull.set(0, filesname);
						foundfilesfull.set(1, filesabs);
						//System.out.println("Found File: " + f.getAbsolutePath());
						break;

					}
					}
				}
				else
				{
					filesname.add(f.getName());
					filesabs.add(f.getAbsolutePath());
					foundfilesfull.set(0, filesname);
					foundfilesfull.set(1, filesabs);

				}
			}
			else if(f.isDirectory())
			{
				ArrayList<ArrayList<String>> dupssub = findDups(f, foundfilesfull);
				dupsfull.get(0).addAll(dupssub.get(0));
				dupsfull.get(1).addAll(dupssub.get(1));
				dupsfull.get(2).addAll(dupssub.get(2));
			}
		}
		return dupsfull;
	}
	
	
	public boolean isSame(File f1, File f2) throws Exception
	{
		boolean same = true;
		
		if(f1.getName().equals(f2.getName())) {
        FileReader fR1 = new FileReader(f1);
        FileReader fR2 = new FileReader(f2);

        BufferedReader reader1 = new BufferedReader(fR1);
        BufferedReader reader2 = new BufferedReader(fR2);

        String line1 = null;
        String line2 = null;
        while (same && ((line1 = reader1.readLine()) != null)
                && ((line2 = reader2.readLine()) != null)) {
            if (!line1.equalsIgnoreCase(line2))
                same = false;
        }
        reader1.close();
        reader2.close();
		}
		else
			same = false;
		
		return same;
	}
	
	public ArrayList<Integer> indexOfAll(String obj, ArrayList<String> list){
	    ArrayList<Integer> indexList = new ArrayList<Integer>();
	    for (int i = 0; i < list.size(); i++)
	        if(obj.equals(list.get(i)))
	            indexList.add(i);
	    return indexList;
	}

    private static String getFileExtension(File file) {
        String fileName = file.getName();
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
        return fileName.substring(fileName.lastIndexOf(".")+1);
        else return "";
    }


}
