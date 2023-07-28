<?php
namespace Database\Seeders;

use Illuminate\Database\Seeder;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Hash;

class MembersTableSeeder extends Seeder
{


    public function run()
    {

        DB::table('members')->update([
            'created_at' => '2023-01-05 12:34:56',
        ]);

        DB::table('members')->insert([
            [
                'member_id' => 'M006',
                'name' => 'kyobe john',
                'username' => 'john',
                'password' => Hash::make('M006'),
                
                'phone_number' => '756839483',
                'created_at' => now(),
                'updated_at' => now()
            ],
            [
                'member_id' => 'M007',
                'name' => 'kabuye Musa',
                'username' => 'musa',
                'password' => Hash::make('M007'),
                
                'phone_number' => '756831283',
                'created_at' => now(),
                'updated_at' => now()
            ],
            [
                'member_id' => 'M008',
                'name' => 'mukisa kenneth',
                'username' => 'kenneth',
                'password' => Hash::make('M008'),
                
                'phone_number' => '756847483',
                'created_at' => now(),
                'updated_at' => now()
            ],
            [
                'member_id' => 'M009',
                'name' => 'naomi kankya',
                'username' => 'kankya',
                'password' => Hash::make('M009'),
                
                'phone_number' => '756889483',
                'created_at' => now(),
                'updated_at' => now()
            ],
            [
                'member_id' => 'M010',
                'name' => 'kikanzi ashiraf',
                'username' => 'ashiraf',
                'password' => Hash::make('M010'),
                
                'phone_number' => '756094483',
                'created_at' => now(),
                'updated_at' => now()
            ],
            [
                'member_id' => 'M011',
                'name' => 'namataka patricia',
                'username' => 'patricia',
                'password' => Hash::make('M011'),
                
                'phone_number' => '756839420',
                'created_at' => now(),
                'updated_at' => now()
            ],
            [
                'member_id' => 'M012',
                'name' => 'assimwe precious',
                'username' => 'precious',
                'password' => Hash::make('M012'),
            
                'phone_number' => '756123483',
                'created_at' => now(),
                'updated_at' => now()
            ],
            [
                'member_id' => 'M013',
                'name' => 'higenyi yurri',
                'username' => 'yurri',
                'password' => Hash::make('M013'),
                
                'phone_number' => '706739483',
                'created_at' => now(),
                'updated_at' => now()
            ],
            [
                'member_id' => 'M014',
                'name' => 'owembabazi jane',
                'username' => 'jane',
                'password' => Hash::make('M014'),
                
                'phone_number' => '709739483',
                'created_at' => now(),
                'updated_at' => now()
            ],
            [
                'member_id' => 'M015',
                'name' => 'ojok Amos',
                'username' => 'amos',
                'password' => Hash::make('M015'),
                
                'phone_number' => '712839483',
                'created_at' => now(),
                'updated_at' => now()
            ],

        ]);
    }
}
