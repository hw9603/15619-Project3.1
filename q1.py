import pandas as pd
s = pd.read_table('yelp_academic_dataset_business.tsv').city.dropna()
print(s[s.str.contains('las vegas', case=False)].count())
