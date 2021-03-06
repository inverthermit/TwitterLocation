#!/bin/bash
#SBATCH -p cloud
#SBATCH --time=01:00:00
#SBATCH --nodes=5
#SBATCH --ntasks=5
#SBATCH --cpus-per-task=1
#SBATCH --mail-user=jluo6@student.unimelb.edu.au
#SBATCH --mail-type=ALL
#SBATCH --output        MyJavaJob.%j.out # Include the job ID in the names of
#SBATCH --error         MyJavaJob.%j.err # the output and error files
module load Java/1.8.0_71
module load mpj/0.44
mpjrun.sh -np 5 TwitterLocation.jar /data/projects/COMP90024/bigTwitter.json ./result/1_5_big_Result.txt
