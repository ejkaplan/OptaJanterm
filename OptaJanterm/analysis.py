import numpy as np
import matplotlib.pyplot as plt
import csv

students = []
groups = []

def read_csv(filename):
    with open(filename) as f:
        rows = list(csv.reader(f))
        for course in rows[0][6:]:
            if course not in groups:
                groups.append(course)
        for row in rows[1:]:
            student = {rows[0][x].lower():row[x].lower() for x in range(5)}
            student["prefs"] = []
            for n in range(1,6):
                if str(n) in row:
                    student["prefs"].append(rows[0][row.index(str(n))])
                else:
                    student["prefs"].append(None)
            students.append(student)

read_csv("9th_grade.csv")
read_csv("10th_grade.csv")
read_csv("11th_grade.csv")
read_csv("12th_grade.csv")

def popularity_plot(topn):
    print(f"Plotting top {topn}")
    demos = ["9m","10m","11m","12m","9f","10f","11f","12f","total"]
    colors = ["#AFEACD","#99D9DB","#23B7C6","#3D3383",
              "#E3A993","#F58D96","#E24B6B","#9F3258"]
    popularity = {group:{demo:0 for demo in demos} for group in groups}
    for student in students:
        try:
            demo = student["grade"]+student["gender"]
            for group in student["prefs"][:topn]:
                if group == None: continue
                popularity[group][demo] += 1
                popularity[group]["total"] += 1
        except:
            pass
    ind = np.arange(len(popularity))
    groups.sort(key = lambda x:popularity[x]["total"])
    nums = [[popularity[x][demo] for x in groups] for demo in demos[:-1]]
    plots = []
    plt.figure(figsize=(20,10))
    for i in range(len(nums)):
        if i == 0:
            plots.append(plt.bar(ind, nums[i], .8, color=colors[i]))
        else:
            plots.append(plt.bar(ind, nums[i], .8, color=colors[i], bottom=[sum(x) for x in zip(*nums[:i])]))
    top = max([popularity[x]["total"] for x in popularity])+10
    plt.ylabel('# of students who put topic in top {}'.format(topn))
    plt.title('Popularity by grade and gender')
    plt.xticks(ind, [f"{x[:25]}..." if len(x) > 20 else x for x in groups], rotation='vertical')
    plt.yticks(np.arange(0,top,10))
    plt.legend([x[0] for x in plots], ["9th grade boys", "10th grade boys", "11th grade boys", "12th grade boys", "9th grade girls", "10th grade girls", "11th grade girls", "12th grade girls"])
    plt.tight_layout()
    plt.grid(axis="y", linestyle="dotted")
    plt.savefig(f'popularity{topn}.png', dpi = 300)

popularity_plot(1)
popularity_plot(3)
popularity_plot(5)
