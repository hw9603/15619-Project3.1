import pandas as pd
s = pd.read_table('yelp_academic_dataset_tip.tsv')
print(s.groupby('user_id')['likes'].sum().max(axis=0))
